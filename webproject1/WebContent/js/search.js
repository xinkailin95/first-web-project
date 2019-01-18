// print data to the page for searching for title
function printTitle(pageNum, data) {
    // alert(pageNum);
    // only records greater than 20 to show the prev and next button
    if (data.length > 20) {
        $("#paging").show();
    } else {
        $("#paging").hide();
    }
    let tBody = $("#table_body");
    // clean tBody when recevied new records
    tBody.html("");
    for (let i = pageNum; i < Math.min(pageNum + 20, jsonData.length); i++) {
        // Concatenate the html tags with resultData jsonObject
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML +=
            "<td>" + data[i]["id"] + '</a>' + "</td>";
        rowHTML +=
            "<td>" + '<a name="titles" href="#" style="text-decoration: none;">' + data[i]["title"] + '</a>' + "</td>";
        rowHTML +=
            "<td>" + data[i]["year"] + "</td>";
        rowHTML +=
            "<td>" + data[i]["director"] + "</td>";
        rowHTML +=
            "<td>" + data[i]["genres"] + "</td>";
        rowHTML +=
            "<td>" + data[i]["ratings"] + "</td>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        tBody.append(rowHTML);
    }

    // if use it outside, there would be error on undefined of data
    // var titles = document.getElementsByName("titles");
    // // alert(titles[0].innerHTML);
    // for (let i = 0; i < titles.length; i++) { // titles[i].onclick=function() { // alert(titles[i].innerHTML); // } // }
    // jquery way for above line
    $("a[name=titles]").click(function(event) {
        // singleMovie.call(this);
        var searchTitle = this.innerHTML;
        $.post('api/movie', { title: searchTitle }, function(data) {
            $("#default-movies").hide && $("#default-movies").hide();
            $("#before-search").hide && $("#before-search").hide();
            $("#after-search").hide && $("#after-search").hide();
            $("#singleStar").hide && $("#singleStar").hide();
            $("#singleMovie").show();
            singleMovie(data);
        });
    });
}

var page = 0;
// handle left page turning
$("#left-page").click(function(event) {
    // alert("test left");
    page -= 20;
    if (page < 0) {
        page = 0;
    }
    printTitle(page, window.jsonData);
});
// hadnle right page turning
$("#right-page").click(function(event) {
    // alert(window.jsonData.length);
    if (page < window.jsonData.length - 20) {
        page += 20;
    }
    printTitle(page, window.jsonData);
});

// show result of searching
// pageNum indicate on which page, dafacult show 20 records on a page
//  - increase or descrease by 20 to change record show on different page
function do_search(blurry, g_type, s_type) {
    // alert(page);
    $.post("api/search", { like: blurry, genre_type: g_type, search_type: s_type }, function(data) {
        // alert(search);
        // alert(data[0]);
        window.jsonData = data;
        if (data.length == 0) {
            $("#display-header").html("Result for '" + blurry + "' not found.");
            $("#paging").hide();
            $("#table_body").html("");
        } else {
            printTitle("0", data);
        }
    }, "json");
}

// for normal search
function reg_search(r_search) {
    event.preventDefault();
    $("#table_body").html("");
    $("#display-header").html("Result for '" + r_search + "'.");
    if (r_search === "") { // the search bar is empty
        // alert("empty");
        $("#display-header").html("Enter movie title or star name to search.");
        $("#paging").hide();
    } else { // do search
        do_search(r_search, "*", "title");
    }
}

// apply when user click on a single movie title
function singleMovie(data) {
    // alert(data);
    var titleData = JSON.parse(data);
    let con = $("#singleMovie");
    con.html("");
    let divHTML = "";
    divHTML += "<h3><span class='col-sm-2'>Title:</span>" + titleData["title"] + "</h3>";
    divHTML += "<h3><span class='col-sm-2'>Year:</span>" + titleData["year"] + "</h3>";
    divHTML += "<h3><span class='col-sm-2'>Director:</span>" + titleData["director"] + "</h3>";
    divHTML += "<h3><span class='col-sm-2'>Stars:</span>" + titleData["star"] + "</h3>";
    con.append(divHTML);

    $("a[name=stars]").click(function(event) {
        // $("#default-movies").hide();
        // $("#singleMovie").show();
        var searchStar = this.innerHTML;
        $.post('api/star', { star: searchStar }, function(data) {
            // alert(data);
            $("#default-movies").hide && $("#default-movies").hide();
            $("#before-search").hide && $("#before-search").hide();
            $("#after-search").hide && $("#after-search").hide();
            $("#singleMovie").hide && $("#singleMovie").hide();
            $("#singleStar").show();
            singleStar(data);
        });
    });
}

function singleStar(data) {
    data = JSON.parse(data);
    // alert(data[0]);
    $("#display-star").html(data[0]["star"] + " (" + data[0]["birthYear"] + ")");
    let sBody = $("#star_body");
    // clean tBody when recevied new records
    sBody.html("");
    for (let i = 0; i < Math.min(20, data.length); i++) {
        // Concatenate the html tags with resultData jsonObject
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML +=
            "<td>" + '<a name="titles" href="#" style="text-decoration: none;">' + data[i]["title"] + '</a>' + "</td>";
        rowHTML +=
            "<td>" + data[i]["year"] + "</td>";
        rowHTML +=
            "<td>" + data[i]["director"] + "</td>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        sBody.append(rowHTML);
    }
    $("a[name=titles]").click(function(event) {
        // singleMovie.call(this);
        var searchTitle = this.innerHTML;
        $.post('api/movie', { title: searchTitle }, function(data) {
            $("#default-movies").hide && $("#default-movies").hide();
            $("#before-search").hide && $("#before-search").hide();
            $("#after-search").hide && $("#after-search").hide();
            $("#singleStar").hide && $("#singleStar").hide();
            $("#singleMovie").show();
            singleMovie(data);
        });
    });
}