const me = requireLogin(); renderHeader("dashboard");
const td = new Date().toISOString().slice(0, 10), mo = td.slice(0, 7);
$("hello").textContent = "Welcome back, " + me.fullName.split(" ")[0] + "!";
$("today").textContent = new Date().toLocaleDateString("en-IN", { day: "numeric", month: "short", year: "numeric" }) + " (Today)";
const safe = p => p.catch(() => null);
(async () => {
    const h = await safe(api("/households/" + me.householdId));
    if (!h) { $("note").hidden = false; return; }
    const [share, sum, gens, exp] = await Promise.all([
        safe(api(`/households/${h.id}/share?date=${td}`)), safe(api(`/households/${h.id}/summary?month=${mo}`)),
        safe(api(`/installations/${h.installationId}/generation`)), safe(api(`/households/${h.id}/exports?month=${mo}`))]);
    if (share) { $("kGen").textContent = k(share.totalGenerated); $("kShare").textContent = k(share.allocatedShare); $("kRatio").textContent = Math.round(share.allocationRatio * 100) + "% of total"; }
    if (sum) {
        $("kCons").textContent = k(sum.totalConsumed); $("kExp").textContent = k(sum.totalExported);
        const net = Number(sum.netUsage);
        $("net").innerHTML = `<div><span>☀️ Allocated share</span><b>${k(sum.totalAllocated)} kWh</b></div><div><span>⚡ Consumed</span><b>${k(sum.totalConsumed)} kWh</b></div>
      <div><span>🔌 Exported</span><b>${k(sum.totalExported)} kWh</b></div>
      <div class="net"><span>⚖️ Net usage</span><b>${k(net)} kWh <small class="${net <= 0 ? "pos" : "neg"}">${net <= 0 ? "Net exporter" : "Net user"}</small></b></div>`;
    }
    if (gens && gens.length) {
        const last = gens.slice(0, 7).reverse(), max = Math.max(...last.map(g => Number(g.totalUnitsGenerated)), 1);
        $("chart").innerHTML = `<div class="chart">${last.map(g => `<div title="${g.totalUnitsGenerated} kWh"><i style="height:${Number(g.totalUnitsGenerated) / max * 100}%"></i></div>`).join("")}</div>
      <div class="labels">${last.map(g => `<span>${g.logDate.slice(5)}</span>`).join("")}</div>`;
    }
    const rows = [];
    (gens || []).slice(0, 4).forEach(g => rows.push([g.logDate, "☀️ Generation", "Solar generation recorded", "+" + k(g.totalUnitsGenerated) + " kWh", "pos"]));
    ((exp && exp.dailyExports) || []).slice(-4).forEach(d => rows.push([d.date, "🔌 Export", "Grid export", "+" + k(d.units) + " kWh", "pos"]));
    rows.sort((a, b) => b[0].localeCompare(a[0]));
    if (rows.length) $("act").innerHTML = `<table><thead><tr><th>Date</th><th>Event</th><th>Details</th><th>Amount</th></tr></thead><tbody>${rows.slice(0, 8).map(r => `<tr><td>${r[0]}</td><td>${r[1]}</td><td>${r[2]}</td><td class="${r[4]}">${r[3]}</td></tr>`).join("")}</tbody></table>`;
})();