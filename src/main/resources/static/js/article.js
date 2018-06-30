window.onload = function () {
    var articleId = getQueryVariable("articleId");

    // 添加文章信息
    $.ajax({
        type: "get",
        url: "http://10.2.3.235:80/api/article/" + articleId,
        dataType: "json",
        success: function (json) {
            // 解析json对象，并向页面添加数据

            $("#articleTitle").html(json.title);
            $("#articleCreateBy").html(json.createBy);
            $("#articleContent").html(json.content);
            Prism.highlightAll();
            $("#articlePicture").attr("src", json.pictureUrl);
        }
    });

    // 添加文章评论信息
    $.ajax({
        type: "get",
        url: "http://10.2.3.235:80/api/comment/article/" + articleId,
        dataType: "json",
        success: function (json) {
            // 解析json对象，并向页面添加数据
            $.each(json, function (i, item) {
                $('#commentList').append(
                    '<div class="comment">' +
                    '<label class="commentName">' + item.name + '</label> <label class="commentTime">' + item.createBy + '</label><br />' +
                    '<lable class="commentContent">' + item.content + '</lable>' +
                    '</div></div>'
                );
            });
        }
    })

}

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

// 增加评论
$('#addComment').click(function () {
    var articleId = getQueryVariable("articleId");
    var name = $('#commentName').val();
    var email = $('#commentEmail').val();
    var content = $('#commentContent').val();

    // 判断是否为空
    if ("" == name || "" == content) {
        $('#modal').modal();
        return;
        return;
    }

    // 不为空才能增加
    var comment = {
        name: name,
        email: email,
        content: content
    }
    // 提交AJAX请求
    $.ajax({
        url: "http://10.2.3.235:80/api/comment/article/" + articleId,
        type: "POST",
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(comment),
        success: function () {
            // 显示成功提示信息
            $('#addModal').modal();
        },
        error: function () {
            $('#addModal').modal();
        }
    })
});

// 模态框确认按钮点击事件
$('#addConfirmBtn').click(function () {
    // 刷新页面
    location.reload();
});