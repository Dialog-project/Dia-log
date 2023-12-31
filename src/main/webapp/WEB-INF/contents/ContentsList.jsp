<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="model.dto.contents.Contents"%>
<%@ page import="model.dto.review.Review"%>
<%@ page import="java.util.*"%>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>ContentsList</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN"
	crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
<link rel="stylesheet"
	href="<c:url value='/css/contents/contentsList.css' />" type="text/css">
<%
List<Contents> contentList = (List<Contents>) request.getAttribute("contentList");
List<Map<String, Object>> reviewList = (List<Map<String, Object>>) request.getAttribute("reviewList");

ObjectMapper mapper = new ObjectMapper();
String jsonContentList = mapper.writeValueAsString(contentList);
String jsonReviewList = mapper.writeValueAsString(reviewList);

%>

<script>
		var contentList = JSON.parse('<%=jsonContentList%>');
		var reviewList = JSON.parse('<%=jsonReviewList%>')

		var cId = 0;
		var uId = 0;
		var flag = 1;
		
        function updateModalContent(contentId) {
            cId = contentId;
            console.log(cId);

            var content = contentList.find(
            		function (c) {
                		return c.contentId == contentId;
            		}
            	);

            localStorage.setItem("contentId", contentId);
            
            if (content) {
                document.getElementById('exampleModalLabel').innerText = content.title;
                document.getElementById('content-genre').innerText = content.genre;
                document.getElementById('content-image').src = content.contentImg;
                
                var formattedDate = new Date(content.publishDate).toLocaleDateString('ko-KR');
                document.getElementById('content-date').innerText = formattedDate;
 
                flag = 1;
                fetchReviews(cId);
            }      
        }
        
        function fetchReviews(contentId) {
            cId = contentId;

            $("#content-review").empty();

            fetch("<c:url value='/contents/reviewList'/>?contentId=" + encodeURIComponent(contentId), {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8'
                },
            })
            .then(response => {
                console.log('Review Response:', response);
                return response.json();
            })
            .then(data => {
                console.log('(Review) Parsed JSON:', data);

                if (data.length > 0) {
                    data.forEach(review => {
                    	if (review.detail !== null) {
                    		flag = 0;
                    		
                    		var star = '★'.repeat(review.rate) + '☆'.repeat(5 - review.rate);
                    		
	                        $("#content-review")
	                            .append(
	                                '<li class="list-group-item d-flex justify-content-between align-items-center">'
	                                + '<a class="social-icon social-icon-journal" href="/dialog/diary?ownerId='
	                                + review.writerId
	                                + '"><i class="bi bi-journal"></i></a>'
	                                + star
	                                + ' :  '
	                                + review.detail
	                                + '</li>');
                    	} 
                   	});
                } 
				if (flag == 1) {
                    $("#content-review")
                        .append('<li class="list-group-item">아직 등록된 리뷰가 없어요. :)</li>');
                }
            })
            .catch(error => {
                console.error('Error fetching reviews:', error.message);
                console.error(error.stack);
            });
        }
        
        function pickContent(userId, contentId) {
        	uId = userId;
        	
            fetch("<c:url value='/contents/pick'/>?userId=" + encodeURIComponent(userId)+"&contentId="+encodeURIComponent(contentId), {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8'
                },
            })
            .then(response => {
            	if (!response.ok) {
                    throw new swal('로그인 후 이용하세요!');
                }
            	console.log('Pick Content Response:', response);
                // return response.json();
            })
            .then(data => {
                console.log('(Content) Parsed JSON:', data);

                swal({
                    title: '내 다이어리로 담아가기',
                    text: '완료되었습니다.',
                    icon: 'success',
                }).then((result) => {
                    if (result) {
                       setTimeout(function () {
                            location.reload();
                        },0);
                    }
                });
            })
            .catch(error => {
                console.error('Error picking content:', error.message);
                console.error(error.stack);
            });
        }
</script>
</head>
<body>
	<%
	if (contentList != null && !contentList.isEmpty()) {
		Iterator<Contents> iterator = contentList.iterator();

		int index = 1;
	%>
	<div class="gallery">
		<div class="container text-center">
			<%
			while (iterator.hasNext()) {
				Contents content = iterator.next();

				if (index % 5 == 1) {
			%>
			<div class="row">
				<%
				}
				%>
				<div
					onclick="cId=<%=content.getContentId()%>, updateModalContent(cId)"
					class="col">
					<article class="card" data-bs-toggle="modal"
						data-bs-target="#exampleModal">
						<figure>
							<img src="<%=content.getContentImg()%>"
								alt="<%=content.getGenre()%>">
							<figcaption>
								<p class="h6"><%=content.getTitle()%></p>
							</figcaption>
						</figure>
					</article>
				</div>

				<div class="modal fade" id="exampleModal" tabindex="-1"
					aria-labelledby="exampleModalLabel" aria-hidden="true">
					<input type="hidden" id="contentId"
						value="<%=content.getContentId()%>">
					<div class="modal-dialog modal-lg">
						<div class="modal-content">
							<div class="modal-header">
								<h1 class="modal-title fs-5" id="exampleModalLabel"></h1>
							</div>
							<div class="modal-body">
								<form>
									<div class="mb-3">
										<label for="recipient-name" class="col-form-label"></label> <img
											style="width: 220px; height: 260px" id="content-image"
											src="<%=content.getContentImg()%>">
									</div>
									<div class="mb-3">
										<label for="recipient-name" class="col-form-label">[genre]</label>
										<p id="content-genre" />
									</div>
									<div class="mb-3">
										<label for="recipient-name" class="col-form-label">[publish-date]</label>
										<p id="content-date" />
									</div>
									<div class="mb-3">
										<label for="message-text" class="col-form-label">[review]</label>
										<ul class="list-group" id="content-review"></ul>
									</div>
								</form>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-danger"
									onclick="pickContent(uId, cId)">담기</button>
							</div>
						</div>
					</div>
				</div>
				<%
				index++;
				}
				%>
				<%
				if (index % 5 == 1) {
				%>
			</div>
			<%
			}
			%>
		</div>
	</div>
	<%
	}
	%>

</body>
</html>

