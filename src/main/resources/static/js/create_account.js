let businessDetails = document.getElementById("business-details");
let privateCustomerButton = document.getElementById("radioCustomer");
let businessCustomerButton = document.getElementById("radioBusiness");

function ready() {
    privateCustomerButton.addEventListener("click", toggleBusinessDetails);
    businessCustomerButton.addEventListener("click", toggleBusinessDetails);
}

function toggleBusinessDetails() {
    if (privateCustomerButton.checked) {
        businessDetails.classList.add("d-none");
        privateCustomerButton.parentElement.classList.add("active")
        businessCustomerButton.parentElement.classList.remove("active")
    } else {
        businessDetails.classList.remove("d-none");
        privateCustomerButton.parentElement.classList.remove("active")
        businessCustomerButton.parentElement.classList.add("active")
    }
}