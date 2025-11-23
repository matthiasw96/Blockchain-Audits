"use strict";

let isConnected = false;
let hasData = false;

document.addEventListener("DOMContentLoaded", init);

function init() {
    const rangeForm = document.getElementById("rangeForm");
    const startDateInput = document.getElementById("startDate");
    const startTimeInput = document.getElementById("startTime");
    const endDateInput = document.getElementById("endDate");
    const endTimeInput = document.getElementById("endTime");

    const btnVerify = document.getElementById("btnVerify");
    const btnDownloadData = document.getElementById("btnDownloadData");
    const btnDownloadReport = document.getElementById("btnDownloadReport");

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

async function handleApplySubmit(event, deps) {
    event.preventDefault();

    const url = createUrl(deps)

    try {
        const response = await sendGetRequest(url);

        const data = await response.json();

        console.log(data)

        if(data.status === "OK") {
            updateStatus(true, data.message, "applyStatus");
            hasData = true;
        } else if(data.code === "ERROR") {
            updateStatus(false, data.message, "applyStatus");
        }
    } catch (err) {
        console.error("Data Selection failed:", err);
        updateStatus(false, "Failed to retrieve data", "applyStatus");
    }
}

async function handleVerifyClick(event) {
    event.preventDefault();

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