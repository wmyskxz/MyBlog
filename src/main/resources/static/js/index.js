// 页面初始化：填充数据
window.onload = function() {
	$.ajax({
		url: "http://10.2.3.235:80/api/article/list/lastest",
		type: "GET",
		dataType: "json",
		success: function(json) {
			$.each(json, function(i, item) {
				// 设置右下角题图的内容
				$(".smallPictures img[location=" + i + "]").attr("src", item.pictureUrl);
				$(".smallPictures img[location=" + i + "]").attr("pictureUrl", item.pictureUrl);
				$(".smallPictures img[location=" + i + "]").attr("articleId", item.id);
				$(".smallPictures img[location=" + i + "]").attr("title", item.title);
				$(".smallPictures img[location=" + i + "]").attr("summary", item.summary);

				// 默认显示第一篇文章的信息
				if(i == "0") {
					$("#articleTitle").html(item.title);
					$("#articleSummary").html(item.summary);
					$("#articlePicture img").attr("src", item.pictureUrl);
					$("#showArticle").attr("articleId", item.id);
				}
			});
		}
	});
};

// 按钮点击进行文章详情页
$("#showArticle").click(function() {
	var articleId = $(this).attr("articleId");
	var url = "article.html?articleId=" + articleId;
	window.location.href = url;
});

// 测试使用的函数
// $("#showArticle").click(function() {
// 	$.ajax({
// 		url: "http://10.2.3.235:80/api/article/1",
// 		type: "GET",
// 		dataType: "json",
// 		success: function(json) {
// 			$("#articleTitle").html(json.title);
// 			$("#articleSummary").html(json.summary);
// 		}
// 	})
// });

// 缩略图鼠标进入事件：更换大图和按钮的articleId
$(".smallPictures img").mouseenter(function() {
	var pictureUrl = $(this).attr("pictureUrl");
	var articleId = $(this).attr("articleId")
	var title = $(this).attr("title");
	var summary = $(this).attr("summary");
	$("#articlePicture img").attr("src", pictureUrl);
	$("#showArticle").attr("articleId", articleId);
	$("#articleTitle").html(title);
	$("#articleSummary").html(summary);
});

// function checkPicurl(url) {
// 	var img = new Image();
// 	img.src = url;
// 	img.onerror = function() {
// 		alert(name + " 图片加载失败，请检查url是否正确");
// 		return false;
// 	};
//
// 	if(img.complete) {
// 		console.log(img.width + " " + img.height);
// 	} else {
// 		img.onload = function() {
// 			console.log(img.width + " " + img.height);
// 			img.onload = null;
// 			//避免重复加载
// 		}
// 	}
// }