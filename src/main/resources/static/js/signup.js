if (currentUser()) location.replace("dashboard.html");
$("formUp").onsubmit = async e => {
    e.preventDefault();
    const f = { fullName: $("upName").value.trim(), email: $("upEmail").value.trim().toLowerCase(), hid: $("upHid").value.trim(),
        pass: $("upPass").value, pass2: $("upPass2").value, ratio: Number($("upRatio").value) };
    const err = m => { $("errUp").textContent = m; };
    if (f.fullName.length < 2) return err("Enter your full name.");
    if (!/^\S+@\S+\.\S+$/.test(f.email)) return err("Enter a valid email address.");
    if (!/^\d+$/.test(f.hid)) return err("Household ID must be a number.");
    if (!validPassword(f.pass)) return err("Password needs 8+ characters with letters and a number.");
    if (f.pass !== f.pass2) return err("Passwords do not match.");
    if (!(f.ratio > 0 && f.ratio <= 1)) return err("Allocation ratio must be between 0 and 1.");
    if (users().some(u => u.email === f.email)) return err("An account with this email already exists.");
    const list = users();
    list.push({ fullName: f.fullName, email: f.email, householdId: f.hid, ratio: f.ratio, address: "",
        passHash: await hash(f.pass), joined: new Date().toISOString().slice(0, 10) });
    saveUsers(list);
    sessionStorage.setItem("ss_session", f.email);
    location.href = "dashboard.html";
};