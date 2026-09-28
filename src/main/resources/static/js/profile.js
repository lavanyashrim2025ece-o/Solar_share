let me = requireLogin(); renderHeader("profile");
document.querySelectorAll(".side button").forEach(b => b.onclick = () => {
    document.querySelectorAll(".side button").forEach(x => x.classList.toggle("on", x === b));
    document.querySelectorAll(".pane").forEach(p => p.classList.toggle("on", p.id === b.dataset.p));
});
function fill() {
    me = currentUser();
    $("av").textContent = me.fullName.split(" ").map(w => w[0]).slice(0, 2).join("").toUpperCase();
    $("pName").textContent = me.fullName; $("pMeta").textContent = "Household #" + me.householdId + ", member since " + me.joined;
    $("fName").value = me.fullName; $("fEmail").value = me.email; $("fHid").value = me.householdId;
    $("fRatio").value = me.ratio; $("fAddr").value = me.address || "";
}
fill();
const update = patch => saveUsers(users().map(u => u.email === me.email ? { ...u, ...patch } : u));
$("formProfile").onsubmit = e => {
    e.preventDefault();
    const name = $("fName").value.trim(), hid = $("fHid").value.trim(), ratio = Number($("fRatio").value);
    const err = m => { $("errP").textContent = m; };
    if (name.length < 2) return err("Enter your full name.");
    if (!/^\d+$/.test(hid)) return err("Household ID must be a number.");
    if (!(ratio > 0 && ratio <= 1)) return err("Allocation ratio must be between 0 and 1.");
    err(""); update({ fullName: name, householdId: hid, ratio, address: $("fAddr").value.trim() });
    fill(); renderHeader("profile"); toast("Profile updated", true);
};
$("formPass").onsubmit = async e => {
    e.preventDefault();
    const err = m => { $("errW").textContent = m; };
    if (me.passHash !== await hash($("pOld").value)) return err("Current password is incorrect.");
    if (!validPassword($("pNew").value)) return err("New password needs 8+ characters with letters and a number.");
    if ($("pNew").value !== $("pNew2").value) return err("New passwords do not match.");
    err(""); update({ passHash: await hash($("pNew").value) }); e.target.reset(); toast("Password updated", true);
};