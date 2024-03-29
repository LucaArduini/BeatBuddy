$(document).ready(function(){
    let value = $("#is_logged").val()
    if(value == 'true')
        categories = ['Song', 'Album', 'Artist', 'User']
    else
        categories = ['Song', 'Album', 'Artist']
    loadSearchBar()
    for(a in categories) {
        $("#category_input").append(
            new Option(categories[a], categories[a].toLowerCase())
        )
    }
    setActiveLink();
})
function loadSearchBar(){
    let value = $("#is_logged").val();
    let admin = $("#is_admin").val();
    $(".header").append(
        "<div class=\"container-fluid d-flex align-items-center justify-content-between\">" +
        "<h1 id=\"logo\" class=\"g-col-3\"><a href=\"/homePage\">BeatBuddy</a></h1>" +
        "<section id=\"search-container\" class=\"w-50\">" +
        "<form class=\"d-flex\">" +
        "<input class=\"form-control\" type=\"text\" id=\"search_input\" placeholder=\"What are you looking for?\">" +
        "<select name=\"category\" id=\"category_input\"></select>" +
        "<button class=\"btn btn-success\" id=\"search_btn\" data-bs-toggle=\"modal\" data-bs-target=\"#search_results\" type=\"button\">Search</button>" +
        "</form>" +
        "</section>" +
        "<nav id=\"navbar\" class=\"navbar justify-content-center g-col-3\">" +
        "<ul class=\"nav nav-underline\">" +
        "<li id=\"home_controller\" class=\"nav-item\" role=\"presentation\">" +
        "<a class=\"nav-link active\" href=\"/homePage\">Home</a>" +
        "</li>" +
        "<li id=\"discover_controller\" class=\"nav-item\" role=\"presentation\">" +
        "</li>" +
        "<li id=\"user_controller\" class=\"nav-item\">" +
        "</li>" +
        "</ul>" +
        "</nav>" +
        "</div>"
    )
    if(value == 'true'){
        if(admin != 'true'){
            $("#discover_controller").append(
                "<a class=\"nav-link scrollto\" href=\"/discoverPage\">Discover</a>"
            )
            if(window.location.href.includes("profilePage")){
                $("#user_controller").append(
                    "<button class=\"btn btn-danger\" id=\"logout_btn\" style=\"border-radius: 10px 10px 10px 10px;\" type=\"button\">Logout</button>"
                )
            }else {
                $("#user_controller").append(
                    "<a href=\"/profilePage\" class=\"btn btn-success\" style=\"border-radius: 10px 10px 10px 10px;\"><i class=\"fa fa-user me-1\"></i>Profile page</a>"
                )
            }
        }else{
            $("#user_controller").empty()
            let container = $("#home_controller")
            container.empty()
            container.append($("<a class=\"nav-link active\" href=\"/adminPage\">Dashboard</a>"))
        }
    }

    $("body").append(
        "<div class=\"modal fade\" id=\"search_results\" data-bs-backdrop=\"static\" tabindex=\"-1\" aria-hidden=\"true\">\n" +
        "  <div class=\"modal-dialog modal-lg modal-dialog-centered\">\n" +
        "    <div class=\"modal-content\">\n" +
        "      <div class=\"modal-header\">\n" +
        "        <h5 class=\"modal-title\" id=\"search_results_title\">Search results</h5>\n" +
        "        <button style=\"color: white;\" id=\"close_btn\" type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>\n" +
        "      </div>\n" +
        "      <div class=\"modal-body\">\n" +
        "      </div>\n" +
        "    </div>\n" +
        "  </div>\n" +
        "</div>"
    )
}
function setActiveLink() {
    // Get the current URL
    var currentUrl = window.location.href;

    // Select all nav-link elements
    var navLinks = document.querySelectorAll('.nav-link');

    // Iterate over each nav-link
    navLinks.forEach(function(navLink) {
        // Remove the active class from all nav-links
        navLink.classList.remove('active');

        // Check if nav-link's href matches the current URL
        if (currentUrl.includes(navLink.getAttribute('href'))) {
            // Add the active class to the matching nav-link
            navLink.classList.add('active');
        }
    });
}