// var num = 0;
// show to default top 20 rated movies
$.post("api/main", function(data) {
    //     alert("hello");
    // console.log(num++);
    let tBody = $("#table_body");
    for (let i = 0; i < data.length; i++) {
        // Concatenate the html tags with resultData jsonObject
        let rowHTML = ""
        rowHTML += "<tr>";
        rowHTML +=
            "<td>" + '<a href="#" style="text-decoration: none;">' + data[i]["title"] + '</a>' + "</td>";
        rowHTML +=
            "<td>" + data[i]["year"] + "</td>";
        rowHTML +=
            "<td>" + data[i]["director"] + "</td>";
        rowHTML +=
            "<td>" + data[i]["genres"] + "</td>";
        rowHTML +=
            "<td>" + '<a href="#" style="text-decoration: none;">' + data[i]["stars"] + '</a>' + "</td>";
        rowHTML +=
            "<td>" + data[i]["ratings"] + "</td>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        tBody.append(rowHTML);
    }
}, "json");

// show result of searching
// pageNum indicate on which page, dafacult show 20 records on a page
// 	- increase or descrease by 20 to change record show on different page
function search(page) {
    var search = $("#search_form input[name=search]").val();
    // alert(page);
    $.post("api/search", { like: search }, function(data) {
        // alert(search);
        // alert(data[0]);
        if (data.length == 0) {
            $("#display-header").html("Result for '" + search + "' not found.");
            $("#paging").hide();
            $("#table_body").html("");
        } else {
            $("#display-header").html("Result for '" + search + "'.");
            // only records greater than 20 to show the prev and next button
            if (data.length > 20) {
                $("#paging").show();
            }

            let tBody = $("#table_body");
            // clean tBody when recevied new records
            tBody.html("");
            for (let i = page; i < Math.min(page + 20, data.length); i++) {
                // Concatenate the html tags with resultData jsonObject
                let rowHTML = "";
                rowHTML += "<tr>";
                rowHTML +=
                    "<td>" + '<a href="#" style="text-decoration: none;">' + data[i]["title"] + '</a>' + "</td>";
                rowHTML +=
                    "<td>" + data[i]["year"] + "</td>";
                "<td>fake</td>";
                rowHTML +=
                    "<td>" + data[i]["director"] + "</td>";
                rowHTML +=
                    "<td>" + data[i]["genres"] + "</td>";
                rowHTML +=
                    "<td>" + '<a href="#" style="text-decoration: none;">' + data[i]["stars"] + '</a>' + "</td>";
                rowHTML +=
                    "<td>" + data[i]["ratings"] + "</td>";
                rowHTML += "</tr>";

                // Append the row created to the table body, which will refresh the page
                tBody.append(rowHTML);
            }
        }
    }, "json");
}

var pageNum = 0;
// when search submit call search() to show the result
$("#search_form").submit(function(event) {
    event.preventDefault();
    if ($("#search_form input[name=search]").val() === "") { // the search bar is empty
        // alert("empty");
        $("#display-header").html("Enter movie title or star name to search.");
        $("#paging").hide();
        $("#table_body").html("");
    } else { // do search
        search("0");
    }
});