"use strict";

let isConnected = false;
let hasData = false;

document.addEventListener("DOMContentLoaded", init);

function init() {
    const hostInput = document.getElementById("host");
    const wavesAddressInput = document.getElementById("wavesAddress");
    const wavesNetSelect = document.getElementById("wavesNet");

    const rangeForm = document.getElementById("rangeForm");
    const startDateInput = document.getElementById("startDate");
    const startTimeInput = document.getElementById("startTime");
    const endDateInput = document.getElementById("endDate");
    const endTimeInput = document.getElementById("endTime");

    const btnSubmit = document.getElementById("btnSubmit");
    const btnApply = document.getElementById("btnApply");
    const btnVerify = document.getElementById("btnVerify");
    const btnDownloadData = document.getElementById("btnDownloadData");
    const btnDownloadReport = document.getElementById("btnDownloadReport");

    if (btnSubmit) {
        btnSubmit.addEventListener("click", (event) =>
            handleSubmitButton(event, { hostInput, wavesAddressInput, wavesNetSelect, btnApply })
        );
    }

    if (rangeForm) {
        rangeForm.addEventListener("submit", (event) =>
            handleApplySubmit(event, { startDateInput, startTimeInput, endDateInput, endTimeInput, btnVerify, btnDownloadData, btnDownloadReport })
        );
    }

    if (btnVerify) {
        btnVerify.addEventListener("click", handleVerifyClick);
    }
    if (btnDownloadData) {
        btnDownloadData.addEventListener("click", handleDownloadDataClick);
    }
    if (btnDownloadReport) {
        btnDownloadReport.addEventListener("click", handleDownloadReportClick);
    }
}


async function handleSubmitButton(event, deps) {
    event.preventDefault();

    const payload = createPayload(deps);

    try {
        const response =  await sendPostRequest(JSON.stringify(payload));

        const data = await response.json();

        if (response.ok) {
            updateStatus(true, "Submitted","submitStatus");
            isConnected = true;
        } else {
            console.error("HTTP Fehler:", response.status);
            updateStatus(false, data.message || "Failed to connect to backend","submitStatus");
        }
    } catch (err) {
        console.error("Setup-Request fehlgeschlagen:", err);
        updateStatus(false, "Failed to connect to backend","submitStatus");
    }
}

async function handleApplySubmit(event, deps) {
    event.preventDefault();

    if(!checkConnection()) {return}

    const url = createUrl(deps)

    try {
        const response = await sendGetRequest(url);

        if(response.ok) {
            updateStatus(true, "Data received", "applyStatus");
            hasData = true;
        } else {
            updateStatus(false, "Failed to retrieve data", "applyStatus");
        }

    } catch (err) {
        console.error("Data Selection failed:", err);
        updateStatus(false, "Failed to connect", "applyStatus");
    }
}

async function handleVerifyClick(event) {
    event.preventDefault();

    if(!checkConnection()) {return}
    if(!checkData()) {return}

    try{
        const response = await sendGetRequest("/verifyData", "application/json");

        if(!response.ok) {
            throw new Error("Status: " + response.status + "Message: " + await response.json())
        }

        const data = await response.json();
        console.log("Antwort vom Backend:", data);
        console.log("Message: ", data.message);

        if(data.message === "true") {
            updateStatus(true, "Data verified", "verifyStatus");
        } else {
            updateStatus(false, "Verification failed", "verifyStatus");
        }
    } catch (err) {
        updateStatus(false, "Failed to connect to backend", "verifyStatus");
        console.error("Failed to connect to backend:", err);
    }
}

async function handleDownloadDataClick(event) {
    event.preventDefault();

    if(!checkConnection()) {return}
    if(!checkData()) {return}

    try{
        const response = await sendGetRequest("/downloadData", "text/csv");

        const blob = await response.blob();

        downloadFile(blob, "data.csv")
    } catch (err) {
        console.error("Downloading Data Failed:", err);
    }
}

async function handleDownloadReportClick(event) {
    event.preventDefault();

    if(!checkConnection()) {return}
    if(!checkData()) {return}

    try{
        const response = await sendGetRequest("/downloadReport","application/pdf");

        const blob = await response.blob();

        downloadFile(blob, "audit-report.pdf");
    } catch (err) {
        console.error("Downloading Report Failed:", err);
    }
}

function sendGetRequest(url, accept) {
    try{
        return fetch(url, {
            method: "GET",
            headers: {
                "Accept": `${accept}`
            }
        });
    } catch (err) {
        console.error(`GET request to ${url} failed: `, err);
        throw err;
    }
}

function sendPostRequest(json) {
    try{
        return fetch("/setup", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            },
            body: json
        });
    } catch (err) {
        console.error("POST request failed:", err);
        throw err;
    }
}

function checkConnection() {
    if (!isConnected) {
        popup("Please submit the connection details first");
        return false;
    }
    return true;
}

function checkData() {
    if (!hasData) {
        popup("Please select data range first!");
        return false;
    }
    return true;
}

function updateStatus(ok, message, element) {
    const statusEl = document.getElementById(element);

    statusEl.classList.remove("status--ok", "status--error");

    if (ok) {
        statusEl.classList.add("status--ok");
        statusEl.textContent = message;
    } else {
        statusEl.classList.add("status--error");
        statusEl.textContent = message;
    }
}

function createPayload(deps) {
    const { hostInput, wavesAddressInput, wavesNetSelect} = deps;
    return {
        bhUri: wavesNetSelect?.value || "",
        bhAddress: wavesAddressInput?.value || "",
        collectorUri: hostInput?.value || ""
    };
}

function createUrl(deps) {
    const { startDateInput, startTimeInput, endDateInput, endTimeInput} = deps;
    const startDate = `${startDateInput?.value}T${startTimeInput?.value}`;
    const endDate = `${endDateInput?.value}T${endTimeInput?.value}`;

    return `/selectData?startDate=${startDate}&endDate=${endDate}`;
}

function downloadFile(blob, filename) {
    const urlObject = URL.createObjectURL(blob);

    const a = document.createElement("a");
    a.href = urlObject;
    a.download = filename;

    document.body.appendChild(a);
    a.click();
    a.remove();

    URL.revokeObjectURL(urlObject);
}

function popup(message) {
    const overlay = document.getElementById("popupOverlay");
    const msg = document.getElementById("popupMessage");

    msg.textContent = message;
    overlay.hidden = false;
}

function closePopup() {
    const overlay = document.getElementById("popupOverlay");
    overlay.hidden = true;
}

document.addEventListener("DOMContentLoaded", () => {
    const btn = document.getElementById("popupCloseBtn");
    btn.addEventListener("click", closePopup);
});