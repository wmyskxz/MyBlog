$(document).ready(function() {
	// 填充文章列表数据
	$.ajax({
		type: "get",
		url: "http://10.2.3.235:80/api/article/list",
		dataType: "json",
		contentType: "application/json;charset=utf-8",
		success: function(json) {
			$.each(json, function(i, item) {
				if(i == 0) {
					// 默认填充第一篇文章的评论信息
					addCommentList(item.id)
				}
				$('#articleList').append('<option articleId="' + item.id + '">' + item.title + '</option>');
			});
			//			alert(firstArticleId);
		}
	});
});

// 根据ID填充评论列表的信息
function addCommentList(id) {
	$.ajax({
		type: "get",
		url: "http://10.2.3.235:80/api/comment/article/" + id,
		dataType: "json",
		contentType: "application/json;charset=utf-8",
		success: function(json) {
			// 先要清空原来的数据
			$('#tbody-comments').html("");
			$.each(json, function(i, item) {
				$('#tbody-comments').append(
					'<tr><td>' + +item.id +
					'</td><td>' + item.content +
					'</td><td>' + item.name +
					'</td><td>' + item.email +
					'</td><td>' + item.ip +
					'</td><td>' + item.createBy +
					'</td><td><button class="btn btn-danger deleteBtn" onclick="deleteArticleComment(\'' + item.articleCommentId + '\')"><i class="fa fa-trash-o"></i>删除</button></td></tr>');
			});
			$('#dataTables-comments').dataTable();
		}
	});
}

// 监听文章Select改变，改变注入相应的评论
document.getElementById("articleList").onchange = function() {
	var articleId = $('#articleList option:selected').attr("articleId");
	//	alert(categoryId);
	addCommentList(articleId);
};

// 删除按钮点击事件
function deleteArticleComment(id) {
	$('#confirmBtn').attr("articleCommentId", id);
	$('#myModal').modal();
};

// 确认删除按钮点击事件
$('#confirmBtn').click(function() {
	var id = $(this).attr("articleCommentId");
	//	alert(id);
	$.ajax({
		type: "DELETE",
		url: "http://10.2.3.235:80/admin/comment/article/" + id,
		success: function() {
			// 刷新页面
			location.reload();
		},
		error: function() {
			location.reload();
		}
	});
});