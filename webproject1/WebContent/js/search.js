// print data to the page
function print(pageNum, data) {
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
            "<td>" + '<a href="#" style="text-decoration: none;">' + data[i]["title"] + '</a>' + "</td>";
        rowHTML +=
            "<td>" + data[i]["year"] + "</td>";
        rowHTML +=
            "<td>" + '<a href="#" style="text-decoration: none;">' + data[i]["director"] + "</td>";
        rowHTML +=
            "<td>" + '<a href="#" style="text-decoration: none;">' + data[i]["genres"] + "</td>";
        rowHTML +=
            "<td>" + data[i]["ratings"] + "</td>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        tBody.append(rowHTML);
    }
}

var page = 0;
// handle left page turning
$("#left-page").click(function(event) {
    // alert("test left");
    page -= 20;
    if (page < 0) {
        page = 0;
    }
    print(page, window.jsonData);
});
// hadnle right page turning
$("#right-page").click(function(event) {
    // alert(window.jsonData.length);
    if (page < window.jsonData.length - 20) {
        page += 20;
    }
    print(page, window.jsonData);
});

// show result of searching
// pageNum indicate on which page, dafacult show 20 records on a page
// 	- increase or descrease by 20 to change record show on different page
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
            $("#display-header").html("Result for '" + blurry + "'.");
            print("0", data);
        }
    }, "json");
}

// for normal search
function reg_search(r_search) {
    event.preventDefault();
    if (r_search === "") { // the search bar is empty
        // alert("empty");
        $("#display-header").html("Enter movie title or star name to search.");
        $("#paging").hide();
        $("#table_body").html("");
    } else { // do search
        do_search(r_search, "*", "title");
    }
}