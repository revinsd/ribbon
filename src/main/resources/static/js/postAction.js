    var deletePostButtonsArray=document.querySelectorAll(".btn-warning");
    deletePostButtonsArray.forEach(e=>{
        e.addEventListener("click",function (){
            var fullPostBlock = e.parentElement.parentElement.parentElement.parentElement.parentElement;
            deletePostAjax(e.id,fullPostBlock);
        })
    })

    function deletePostAjax(postId, element){
        var jsonData = {};
        jsonData["postId"]=parseInt(postId);
        $.ajax({
                url: "/deletePost",
                type: "POST",
                contentType : "application/json",
                dataType:"json",
                data: JSON.stringify(jsonData),
                success: function (){
                    element.style.display="none";
                }
            }
        )
    }