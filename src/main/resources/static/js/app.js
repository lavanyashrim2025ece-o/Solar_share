const $ = id => document.getElementById(id);
const esc = s => String(s ?? "").replace(/[&<>"']/g, c => ({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;"}[c]));
const k = v => Number(v || 0).toFixed(1);
const users = () => JSON.parse(localStorage.getItem("ss_users") || "[]");
const saveUsers = u => localStorage.setItem("ss_users", JSON.stringify(u));
async function hash(t) {
    const b = await crypto.subtle.digest("SHA-256", new TextEncoder().encode(t));
    return [...new Uint8Array(b)].map(x => x.toString(16).padStart(2, "0")).join("");
}
const session = () => localStorage.getItem("ss_session") || sessionStorage.getItem("ss_session");
const currentUser = () => users().find(u => u.email === session()) || null;
function requireLogin() { const u = currentUser(); if (!u) { location.replace("index.html"); return null; } return u; }
function logout() { localStorage.removeItem("ss_session"); sessionStorage.removeItem("ss_session"); location.href = "index.html"; }
const validPassword = p => p.length >= 8 && /[A-Za-z]/.test(p) && /\d/.test(p);
const NAV = [["dashboard","dashboard.html","🏠 Dashboard"],["households","records.html#households","👥 Households"],
    ["generation","records.html#generation","☀️ Generation"],["consumption","records.html#consumption","⚡ Consumption"],["profile","profile.html","👤 Profile"]];
function setNav(key) { document.querySelectorAll("#topbar nav a").forEach(a => a.classList.toggle("on", a.dataset.k === key)); }
function renderHeader(active) {
    const u = currentUser();
    const ini = u.fullName.split(" ").map(w => w[0]).slice(0, 2).join("").toUpperCase();
    $("topbar").innerHTML = `<a class="logo" href="dashboard.html"><img src="img/logo.svg" alt=""><span>SolarShare</span></a>
    <nav>${NAV.map(n => `<a data-k="${n[0]}" href="${n[1]}">${n[2]}</a>`).join("")}</nav>
    <div class="me"><span class="chip">${esc(ini)}</span><button class="out" onclick="logout()">Logout ↪</button></div>`;
    setNav(active);
}
function toast(msg, ok) {
    const t = $("toast"); t.textContent = msg; t.className = ok ? "ok" : "bad"; t.style.display = "block";
    clearTimeout(toast.t); toast.t = setTimeout(() => t.style.display = "none", 3500);
}
async function api(path, method = "GET", body) {
    const res = await fetch("/api" + path, { method, headers: { "Content-Type": "application/json" }, body: body ? JSON.stringify(body) : undefined });
    if (res.status === 204) return null;
    const data = await res.json().catch(() => ({}));
    if (!res.ok) throw new Error(data.message || "Request failed (" + res.status + ")");
    return data;
}
async function run(fn, ok) { try { const r = await fn(); if (ok) toast(ok, true); return r; } catch (e) { toast(e.message, false); } }
document.addEventListener("click", e => {
    if (e.target.classList.contains("eye")) { const i = e.target.previousElementSibling; i.type = i.type === "password" ? "text" : "password"; }
});