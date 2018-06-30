// 页面初始化：填充数据
window.onload = function () {
    $.ajax({
        url: "http://10.2.3.235:80/api/category/list/",
        type: "GET",
        dataType: "json",
        success: function (json) {

            // 先填充分类信息
            $.each(json, function (i, item) {
                // 填充分类信息
                var categoryInfo = document.querySelector("#categoryInfo");
                categoryInfo.content.querySelector("a").innerHTML = item.name + "(" + item.number + ")";
                categoryInfo.content.querySelector("a").href = "?categoryId=" + item.id;
                document.getElementById("category").appendChild(categoryInfo.content.cloneNode(true));
            });

            // 判断当前分类情况
            var categoryId = getQueryVariable("categoryId");
            //			alert(categoryId);
            if (categoryId == false) {

                // 如果没有指定分类则获取全部文章
                showAllArticleInfo();
            } else {
                // 有指定分类则显示指定分类下的文章
                showArticleByCategoryId(categoryId);
            }

        }
    });
};

// 获取网页中的参数
function getQueryVariable(variable) {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] == variable) {
            return pair[1];
        }
    }
    return (false);
}

// 显示全部文章信息
function showAllArticleInfo() {

    $.ajax({
        type: "get",
        url: "http://10.2.3.235:80/api/article/list",
        dataType: "json",
        success: function (json) {
            $.each(json, function (i, item) {
                // 填充文章信息
                var articleInfo = document.querySelector("#articleInfo");
                articleInfo.content.querySelector("img").src = item.pictureUrl;
                if (item.top == true) {
                    articleInfo.content.querySelector("h5").innerHTML = "[置顶]" + item.title;
                    //					articleInfo.content.querySelector("h5").style.fontWeight = "bold";
                } else {
                    articleInfo.content.querySelector("h5").innerHTML = item.title;
                }
                articleInfo.content.querySelector("h6").innerHTML = item.id;
                document.getElementById("articleInfos").appendChild(articleInfo.content.cloneNode(true));

            });
        }
    });
}

// 显示指定分类下的文章列表
function showArticleByCategoryId(id) {
    $.ajax({
        type: "get",
        url: "http://10.2.3.235:80/api/article/list/sort/" + id,
        dataType: "json",
        success: function (json) {
            $.each(json, function (i, item) {
                // 填充文章信息
                var articleInfo = document.querySelector("#articleInfo");
                articleInfo.content.querySelector("img").src = item.pictureUrl;
                if (item.top == true) {
                    articleInfo.content.querySelector("h5").innerHTML = "[置顶]" + item.title;
                    //					articleInfo.content.querySelector("h5").style.fontWeight = "bold";
                } else {
                    articleInfo.content.querySelector("h5").innerHTML = item.title;
                }
                articleInfo.content.querySelector("h6").innerHTML = item.id;
                document.getElementById("articleInfos").appendChild(articleInfo.content.cloneNode(true));
            });
        }
    });
}

// 跳转到指定文章
function showArticle(_this) {
    var articleId = $(_this).children("h6").text();
    var url = "article.html?articleId=" + articleId;
    window.location.href = url;
}