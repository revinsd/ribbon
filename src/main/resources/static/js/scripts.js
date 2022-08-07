    const header = document.getElementById("header");
    if(header!=null) {
        const sticky = header.offsetTop;
        window.onscroll = function () {
            if (window.pageYOffset > sticky) {
                header.classList.add("sticky");
            } else {
                header.classList.remove("sticky");
            }
        };
    }


    const forms = document.querySelectorAll('.needs-validation')
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault()
                event.stopPropagation()
            }

            form.classList.add('was-validated')
        }, false)
    })

    if(document.getElementById("user-existence")!=null){
        const ratingBtnsArray = document.querySelectorAll(".rating-btn");
        ratingBtnsArray.forEach(e => {
            e.addEventListener('click', function () {
                var ratingCountBtn = e.parentElement.getElementsByClassName("rating-count-btn")[0];
                var postId = parseInt(e.parentElement.getElementsByClassName("post-id")[0].value);
                if (e.innerHTML == "+") {
                    postAjax(postId, "+", ratingCountBtn);
                    if (!e.classList.contains("rated-plus")) {
                        e.classList.add("rated-plus");
                        try {
                            e.parentElement.getElementsByClassName("rated-minus")[0].classList.remove("rated-minus");
                        } catch (e) {
                            console.log(e);
                        }
                    } else {
                        e.classList.remove("rated-plus")
                    }
                } else if (e.innerHTML == "-") {
                    postAjax(postId, "-", ratingCountBtn);
                    if (!e.classList.contains("rated-minus")) {
                        e.classList.add("rated-minus");
                        try {
                            e.parentElement.getElementsByClassName("rated-plus")[0].classList.remove("rated-plus");
                        } catch (e) {
                            console.log(e);
                        }
                    } else {
                        e.classList.remove("rated-minus");
                    }
                }
            })
        })
    }

    function postAjax(postId, operationType, ratingCountBtn){
        var jsonData = {};
        jsonData["postId"]=postId;
        jsonData["operationType"]=operationType;
        $.ajax({
                url: "/changePostRating",
                type: "POST",
                contentType : "application/json",
                dataType:"json",
                data: JSON.stringify(jsonData),
                success: function (response) {
                    ratingCountBtn.innerHTML= response["postRating"];
                }
            }
        )
    }

    var toastActivator = document.getElementById("toastActivator");
    if(toastActivator!=null){
        var toast= $('#liveToast').show();
            document.getElementById('toast-close').addEventListener("click",function (){
            toast.hide();
        })
    }


