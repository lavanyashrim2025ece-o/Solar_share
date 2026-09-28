if (currentUser()) location.replace("dashboard.html");
$("formIn").onsubmit = async e => {
    e.preventDefault();
    const email = $("inEmail").value.trim().toLowerCase(), pass = $("inPass").value;
    if (!email || !pass) { $("errIn").textContent = "Enter your email and password."; return; }
    const u = users().find(x => x.email === email);
    if (!u || u.passHash !== await hash(pass)) { $("errIn").textContent = "Email or password is incorrect."; return; }
    (($("remember").checked) ? localStorage : sessionStorage).setItem("ss_session", u.email);
    location.href = "dashboard.html";
};