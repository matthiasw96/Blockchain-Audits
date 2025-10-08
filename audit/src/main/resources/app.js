const $ = (sel) => document.querySelector(sel);

function toast(msg, ms = 1800) {
    const el = $("#toast");
    el.textContent = msg;
    el.hidden = false;
    setTimeout(() => (el.hidden = true), ms);
}

async function fetchJSON(url, opts) {
    const res = await fetch(url, { headers: { "Accept": "application/json" }, ...opts });
    if (!res.ok) {
        const text = await res.text().catch(() => "");
        throw new Error(`HTTP ${res.status}: ${text || res.statusText}`);
    }
    return res.json().catch(() => res.text()); // allows plain text API too
}

async function loadSummary() {
    const out = $("#output");
    out.textContent = "Loading…";
    try {
        const data = await fetchJSON("/api/summary");
        out.textContent = typeof data === "string" ? data : JSON.stringify(data, null, 2);
        toast("Summary loaded");
    } catch (e) {
        out.textContent = `Error: ${e.message}`;
        toast("Failed to load", 2000);
    }
}

function init() {
    $("#year").textContent = new Date().getFullYear();
    $("#loadBtn").addEventListener("click", loadSummary);
}

document.addEventListener("DOMContentLoaded", init);
// --- Utilities ---
const pad = (n) => String(n).padStart(2, "0");

// Formatters to your required shapes
function fmtDateYYYYMMDD(d) {
    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}`;
}
function fmtTimeHHmm(d) {
    return `${pad(d.getHours())}:${pad(d.getMinutes())}`;
}

// Round a Date to nearest minute (down)
function floorToMinute(d) {
    const copy = new Date(d);
    copy.setSeconds(0, 0);
    return copy;
}

// --- Initialize default values: end=now, start=end-1h ---
function initDatePickers() {
    const now = floorToMinute(new Date());
    const oneHourAgo = new Date(now.getTime() - 60 * 60 * 1000);

    document.getElementById("endDate").value = fmtDateYYYYMMDD(now);
    document.getElementById("endTime").value = fmtTimeHHmm(now);

    document.getElementById("startDate").value = fmtDateYYYYMMDD(oneHourAgo);
    document.getElementById("startTime").value = fmtTimeHHmm(oneHourAgo);
}

// Build JS Date from two inputs (local time)
function buildDate(dateStr, timeStr) {
    // dateStr: YYYY-MM-DD, timeStr: HH:mm
    const [y, m, d] = dateStr.split("-").map(Number);
    const [hh, mm] = timeStr.split(":").map(Number);
    return new Date(y, m - 1, d, hh, mm, 0, 0);
}

// Validate: start <= end
function validateRange(start, end) {
    if (isNaN(start.getTime()) || isNaN(end.getTime())) {
        throw new Error("Please select valid dates and times.");
    }
    if (start > end) {
        throw new Error("Start must be before or equal to End.");
    }
}

// Example: call backend with the chosen range
async function submitRange(e) {
    e.preventDefault();
    const startDate = document.getElementById("startDate").value;
    const startTime = document.getElementById("startTime").value;
    const endDate = document.getElementById("endDate").value;
    const endTime = document.getElementById("endTime").value;

    const start = buildDate(startDate, startTime);
    const end = buildDate(endDate, endTime);

    try {
        validateRange(start, end);

        // If your API wants strings:
        const params = new URLSearchParams({
            fromDate: startDate,           // YYYY-MM-DD
            fromTime: startTime,           // HH:mm
            toDate: endDate,
            toTime: endTime,
        });

        // Or, if your API wants ISO: start.toISOString(), end.toISOString()

        // Sample request to your endpoint:
        const data = await fetchJSON(`/api/summary?${params.toString()}`);

        // Show result (reusing your existing output area)
        const out = document.getElementById("output");
        out.textContent = typeof data === "string" ? data : JSON.stringify(data, null, 2);
        toast("Range applied");
    } catch (err) {
        toast(err.message, 2500);
    }
}

// Hook everything up
function initRangeForm() {
    initDatePickers();
    document.getElementById("rangeForm").addEventListener("submit", submitRange);
}

// Call from your existing init()
function init() {
    document.getElementById("year").textContent = new Date().getFullYear();
    document.getElementById("loadBtn").addEventListener("click", loadSummary);
    initRangeForm(); // <-- add this
}

document.addEventListener("DOMContentLoaded", init);
