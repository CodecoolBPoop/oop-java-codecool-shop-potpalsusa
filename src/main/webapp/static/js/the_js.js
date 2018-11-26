function main() {

    function cartItemNumber() {
        let addButtons = document.getElementsByClassName("add");
        let cart = document.getElementById("cart");

        for (let button of addButtons) {
            button.addEventListener("click", function () {
                let productId = button.getAttribute("name");
                let params = {itemId: productId};
                $.post('/', $.param(params), function () {
                    console.log("Successfully added.");
                });
                let cartNum = parseInt(cart.innerHTML) + 1;
                cart.innerHTML = cartNum;
            })
        }
    }

    function updateCart(){
        let updateButtons = document.getElementsByClassName("update");

        for(let button of updateButtons){
            button.addEventListener("click", function () {
                let productId = button.getAttribute("name");
                let originalNum = document.getElementById(productId).getAttribute("value");
                let productNum = $("#" + productId).val();
                if (parseInt(productNum) < 0 || isNaN(productNum)){
                    productNum = originalNum;
                    $("#" + productId).val(originalNum);
                } else if(parseInt(productNum) === 0){
                    removeFromCartToZero(productId);
                }

                let params = {itemId : productId, quantity : productNum};
                $.post('/shopping-cart', $.param(params), function () {
                    console.log("Successfully POST method.");
                    recalculateTotal();
                });
            })

        }
    }

    function recalculateTotal(){
        let prices = document.getElementsByClassName("price");
        let total = document.getElementById("total");
        let newTotal = 0;

        let cookies = document.cookie.split(";");
        for(let cookie of cookies){
            let splittedCookie = cookie.split("=");
            let cookieItemId = splittedCookie[0].trim();
            let quantity = parseInt(splittedCookie[1]);
            for(let price of prices){
                if(price.getAttribute("name") == cookieItemId){
                    newTotal += (quantity * (parseFloat(price.getAttribute("value"))));
                }
            }
        }
        let outPut = "Total: " + newTotal + " USD";
        total.innerHTML = outPut;
    }

    function removeFromCart(){
        let removeButtons = document.getElementsByClassName("remove");
        for(let button of removeButtons){
            button.addEventListener("click", function () {
                let itemId = button.getAttribute("name");
                document.cookie = itemId + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
                let item = document.getElementById("item" + itemId);
                item.parentElement.removeChild(item);
                recalculateTotal();
            })
        }
    }

    function removeFromCartToZero(itemId){
        document.cookie = itemId + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
        let item = document.getElementById("item" + itemId);
        item.parentElement.removeChild(item);
    }

    cartItemNumber();
    updateCart();
    removeFromCart();

}
main();

