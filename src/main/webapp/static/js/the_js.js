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
}
main();

