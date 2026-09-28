const me = requireLogin(); renderHeader("households");
const today = new Date().toISOString().slice(0, 10);
["gDate", "cDate"].forEach(i => $(i).value = today);
const num = id => $(id).value === "" ? null : Number($(id).value);
const opts = (l, f) => l.length ? l.map(x => `<option value="${x.id}">${esc(f(x))}</option>`).join("") : `<option value="">None yet</option>`;
function show() {
    const key = (location.hash || "#households").slice(1);
    ["households", "generation", "consumption"].forEach(s => $(s).classList.toggle("on", s === key));
    setNav(key); if (key === "generation") loadGens();
}
addEventListener("hashchange", show);
async function refresh() {
    const ins = await run(() => api("/installations")) || [], hs = await run(() => api("/households")) || [];
    $("instRows").innerHTML = ins.map(i => `<tr><td>${i.id}</td><td>${esc(i.name)}</td><td>${esc(i.location)}</td><td>${i.capacityKw}</td></tr>`).join("");
    $("houseRows").innerHTML = hs.map(h => `<tr><td>${h.id}</td><td>${esc(h.name)}</td><td>${esc(h.ownerName)}</td><td>${h.allocationRatio}</td></tr>`).join("");
    const io = opts(ins, i => `${i.name} (#${i.id})`); $("hInst").innerHTML = io; $("gInst").innerHTML = io;
    $("cHouse").innerHTML = opts(hs, h => `${h.name}, ${h.ownerName} (#${h.id})`);
    if (hs.some(h => String(h.id) === String(me.householdId))) $("cHouse").value = me.householdId;
}
async function addInstallation() {
    const r = await run(() => api("/installations", "POST", { name: $("iName").value, location: $("iLoc").value, capacityKw: num("iCap") }), "Installation saved");
    if (r) { ["iName", "iLoc", "iCap"].forEach(i => $(i).value = ""); refresh(); }
}
async function addHousehold() {
    const r = await run(() => api("/households", "POST", { installationId: $("hInst").value ? Number($("hInst").value) : null, name: $("hName").value, ownerName: $("hOwner").value, allocationRatio: num("hRatio") }), "Household saved");
    if (r) { ["hName", "hOwner", "hRatio"].forEach(i => $(i).value = ""); refresh(); }
}
async function loadGens() {
    const id = $("gInst").value; if (!id) { $("genRows").innerHTML = ""; return; }
    const rows = await run(() => api(`/installations/${id}/generation`));
    if (rows) $("genRows").innerHTML = rows.map(g => `<tr><td>${g.logDate}</td><td>${g.totalUnitsGenerated}</td></tr>`).join("");
}
$("gInst").onchange = loadGens;
async function addGeneration() {
    const r = await run(() => api(`/installations/${$("gInst").value}/generation`, "POST", { logDate: $("gDate").value, totalUnitsGenerated: num("gUnits") }), "Generation saved");
    if (r) { $("gUnits").value = ""; loadGens(); }
}
async function addConsumption() {
    const r = await run(() => api(`/households/${$("cHouse").value}/consumption`, "POST", { logDate: $("cDate").value, unitsConsumed: num("cUnits") }), "Consumption saved");
    if (r) { $("cUnits").value = ""; $("consOut").innerHTML = `<div class="stats"><div class="stat"><b>${k(r.allocatedShare)}</b><span>Allocated share</span></div><div class="stat"><b>${k(r.unitsConsumed)}</b><span>Consumed</span></div><div class="stat"><b>${k(r.unitsExported)}</b><span>Exported</span></div></div>`; }
}
refresh().then(show);