$(document).ready(function () {

    var dialog = $(".dialog-"+productId).dialog({
        autoOpen: false,
        buttons: [{
            text: "Proceed",
            click: function () {
                console.log(productId)
                var ime = $("#ime").val();
                var prezime = $("#prezime").val();
                if (ime == "" || prezime == "") {
                    alert("Zadolzitelno vnesete ime i prezime")
                } else {
                    $("#rezz").val(ime + " " + prezime);
                    dialog.dialog('close');
                }
            }
        }]
    });
    var productId
    $(".click").click(function () {
        var listClass=this.getAttribute("class")
         productId=listClass.split(" ")[0]
            // console.log("hi")
        console.log(productId)
        dialog.dialog('open');
    })
})
