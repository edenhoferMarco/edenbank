document.getElementById("business-details");
let businessForm = document.getElementById("business-customer-form-row");
let privateForm = document.getElementById("private-customer-form-row");
let privateCustomerButton = document.getElementById("radioCustomer");
let businessCustomerButton = document.getElementById("radioBusiness");

function ready() {
    privateCustomerButton.addEventListener("click", toggleBusinessDetails);
    businessCustomerButton.addEventListener("click", toggleBusinessDetails);
}

function toggleBusinessDetails() {
    if (privateCustomerButton.checked) {
        businessForm.classList.add("d-none");
        privateForm.classList.remove("d-none");
        privateCustomerButton.parentElement.classList.add("active");
        businessCustomerButton.parentElement.classList.remove("active")
    } else {
        businessForm.classList.remove("d-none");
        privateForm.classList.add("d-none");
        privateCustomerButton.parentElement.classList.remove("active");
        businessCustomerButton.parentElement.classList.add("active")
    }
}