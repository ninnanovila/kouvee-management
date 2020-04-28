package com.example.kouveemanagement.presenter

import com.example.kouveemanagement.model.*
import com.example.kouveemanagement.repository.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody

//LOGIN
class LoginPresenter(private val view: LoginView, private val repository: Repository) {

    fun loginPost(id: String, password: String) {
        view.showLoginLoading()
        repository.loginPost(id, password, object : LoginRepositoryCallback<LoginResponse> {
            override fun loginSuccess(data: LoginResponse?) {
                view.loginSuccess(data)
                view.hideLoginLoading()
            }
            override fun loginFailed(data: String) {
                view.loginFailed(data)
                view.hideLoginLoading()
            }
        })
    }
}

//EMPLOYEE
class EmployeePresenter(private val view: EmployeeView, private val repository: Repository) {

    fun getAllEmployee(){
        view.showEmployeeLoading()
        repository.getAllEmployee(object : EmployeeRepositoryCallback<EmployeeResponse> {
            override fun employeeSuccess(data: EmployeeResponse?) {
                view.employeeSuccess(data)
                view.hideEmployeeLoading()
            }
            override fun employeeFailed(data: String) {
                view.employeeFailed(data)
                view.hideEmployeeLoading()
            }
        })
    }

    fun getEmployeeBySearch(query: String){
        view.showEmployeeLoading()
        repository.getEmployeeBySearch(query, object : EmployeeRepositoryCallback<EmployeeResponse>{
            override fun employeeSuccess(data: EmployeeResponse?) {
                view.employeeSuccess(data)
                view.hideEmployeeLoading()
            }
            override fun employeeFailed(data: String) {
                view.employeeFailed(data)
                view.hideEmployeeLoading()
            }
        })
    }

    fun addEmployee(employee: Employee){
        view.showEmployeeLoading()

        repository.addEmployee(employee, object : EmployeeRepositoryCallback<EmployeeResponse> {
            override fun employeeSuccess(data: EmployeeResponse?) {
                view.employeeSuccess(data)
                view.hideEmployeeLoading()
            }
            override fun employeeFailed(data: String) {
                view.employeeFailed(data)
                view.hideEmployeeLoading()
            }

        })
    }

    fun editEmployee(id: String, employee: Employee){
        view.showEmployeeLoading()

        repository.editEmployee(id, employee, object : EmployeeRepositoryCallback<EmployeeResponse> {
            override fun employeeSuccess(data: EmployeeResponse?) {
                view.employeeSuccess(data)
                view.hideEmployeeLoading()
            }
            override fun employeeFailed(data: String) {
                view.employeeFailed(data)
                view.hideEmployeeLoading()
            }
        })
    }

    fun deleteEmployee(id: String){
        view.showEmployeeLoading()

        repository.deleteEmployee(id, object : EmployeeRepositoryCallback<EmployeeResponse> {
            override fun employeeSuccess(data: EmployeeResponse?) {
                view.employeeSuccess(data)
                view.hideEmployeeLoading()
            }
            override fun employeeFailed(data: String) {
                view.employeeFailed(data)
                view.hideEmployeeLoading()
            }
        })
    }
}

//PRODUCT
class ProductPresenter(private val view : ProductView, private val repository: Repository){

    fun getAllProduct(){
        view.showProductLoading()
        repository.getAllProduct(object : ProductRepositoryCallback<ProductResponse> {
            override fun productSuccess(data: ProductResponse?) {
                view.productSuccess(data)
                view.hideProductLoading()
            }
            override fun productFailed(data: String) {
                view.productFailed(data)
                view.hideProductLoading()
            }
        })
    }

    fun getProductBySearch(query: String){
        view.showProductLoading()
        repository.getProductBySearch(query, object : ProductRepositoryCallback<ProductResponse> {
            override fun productSuccess(data: ProductResponse?) {
                view.productSuccess(data)
                view.hideProductLoading()
            }
            override fun productFailed(data: String) {
                view.productFailed(data)
                view.hideProductLoading()
            }
        })
    }

    fun addProduct(product: Product){
        view.showProductLoading()
        repository.addProduct(product, object : ProductRepositoryCallback<ProductResponse> {
            override fun productSuccess(data: ProductResponse?) {
                view.productSuccess(data)
                view.hideProductLoading()
            }
            override fun productFailed(data: String) {
                view.productFailed(data)
                view.hideProductLoading()
            }
        })
    }

    fun editProduct(id: String, product: Product){
        view.showProductLoading()
        repository.editProduct(id, product, object : ProductRepositoryCallback<ProductResponse> {
            override fun productSuccess(data: ProductResponse?) {
                view.productSuccess(data)
                view.hideProductLoading()
            }
            override fun productFailed(data: String) {
                view.productFailed(data)
                view.hideProductLoading()
            }
        })
    }

    fun deleteProduct(id: String){
        view.showProductLoading()
        repository.deleteProduct(id, object : ProductRepositoryCallback<ProductResponse> {
            override fun productSuccess(data: ProductResponse?) {
                view.productSuccess(data)
                view.hideProductLoading()
            }
            override fun productFailed(data: String) {
                view.productFailed(data)
                view.hideProductLoading()
            }
        })
    }


}

//UPLOAD IMAGE
class UploadImagePresenter(private val view: UploadPhotoProductView, private val repository: Repository){

    fun uploadPhotoProduct(id: String, photo: MultipartBody.Part){
        view.showUploadProgress()
        repository.uploadPhotoProduct(id, photo, object : UploadPhotoProductRepositoryCallback<ResponseBody> {
            override fun uploadProductSuccess(data: ResponseBody?) {
                view.uploadProductSuccess(data)
                view.hideUploadProgress()
            }
            override fun uploadProductFailed(data: String) {
                view.uploadProductFailed(data)
                view.hideUploadProgress()
            }
        })
    }
}

//CUSTOMER
class CustomerPresenter(private val view: CustomerView, private val repository: Repository){

    fun getAllCustomer(){
        view.showCustomerLoading()
        repository.getAllCustomer(object : CustomerRepositoryCallback<CustomerResponse> {
            override fun customerSuccess(data: CustomerResponse?) {
                view.customerSuccess(data)
                view.hideCustomerLoading()
            }
            override fun customerFailed(data: String) {
                view.customerFailed(data)
                view.hideCustomerLoading()
            }
        })
    }

    fun getCustomerBySearch(query: String){
        view.showCustomerLoading()
        repository.getCustomerBySearch(query, object : CustomerRepositoryCallback<CustomerResponse>{
            override fun customerSuccess(data: CustomerResponse?) {
                view.customerSuccess(data)
                view.hideCustomerLoading()
            }
            override fun customerFailed(data: String) {
                view.customerFailed(data)
                view.hideCustomerLoading()
            }
        })
    }

    fun addCustomer(customer: Customer){
        view.showCustomerLoading()
        repository.addCustomer(customer,object : CustomerRepositoryCallback<CustomerResponse> {
            override fun customerSuccess(data: CustomerResponse?) {
                view.customerSuccess(data)
                view.hideCustomerLoading()
            }
            override fun customerFailed(data: String) {
                view.customerFailed(data)
                view.hideCustomerLoading()
            }
        })
    }

    fun editCustomer(id: String, customer: Customer){
        view.showCustomerLoading()
        repository.editCustomer(id, customer, object : CustomerRepositoryCallback<CustomerResponse> {
            override fun customerSuccess(data: CustomerResponse?) {
                view.customerSuccess(data)
                view.hideCustomerLoading()
            }
            override fun customerFailed(data: String) {
                view.customerFailed(data)
                view.hideCustomerLoading()
            }
        })
    }

    fun deleteCustomer(id: String, last_emp: String){
        view.showCustomerLoading()
        repository.deleteCustomer(id,last_emp, object : CustomerRepositoryCallback<CustomerResponse> {
            override fun customerSuccess(data: CustomerResponse?) {
                view.customerSuccess(data)
                view.hideCustomerLoading()
            }
            override fun customerFailed(data: String) {
                view.customerFailed(data)
                view.hideCustomerLoading()
            }
        })
    }
}

//PET SIZE
class PetSizePresenter(private val view: PetSizeView, private val repository: Repository){

    fun getAllPetSize(){
        view.showPetSizeLoading()
        repository.getAllPetSize(object : PetSizeRepositoryCallback<PetSizeResponse> {
            override fun petSizeSuccess(data: PetSizeResponse?) {
                view.petSizeSuccess(data)
                view.hidePetSizeLoading()
            }
            override fun petSizeFailed(data: String) {
                view.petSizeFailed(data)
                view.hidePetSizeLoading()
            }
        })
    }

    fun getPetSizeBySearch(query: String){
        view.showPetSizeLoading()
        repository.getPetSizeBySearch(query, object : PetSizeRepositoryCallback<PetSizeResponse>{
            override fun petSizeSuccess(data: PetSizeResponse?) {
                view.petSizeSuccess(data)
                view.hidePetSizeLoading()
            }
            override fun petSizeFailed(data: String) {
                view.petSizeFailed(data)
                view.hidePetSizeLoading()
            }
        })
    }

    fun addPetSize(petSize: PetSize){
        view.showPetSizeLoading()
        repository.addPetSize(petSize, object : PetSizeRepositoryCallback<PetSizeResponse> {
            override fun petSizeSuccess(data: PetSizeResponse?) {
                view.petSizeSuccess(data)
                view.hidePetSizeLoading()
            }
            override fun petSizeFailed(data: String) {
                view.petSizeFailed(data)
                view.hidePetSizeLoading()
            }
        })
    }

    fun editPetSize(id: String, petSize: PetSize){
        view.showPetSizeLoading()
        repository.editPetSize(id, petSize, object : PetSizeRepositoryCallback<PetSizeResponse> {
            override fun petSizeSuccess(data: PetSizeResponse?) {
                view.petSizeSuccess(data)
                view.hidePetSizeLoading()
            }
            override fun petSizeFailed(data: String) {
                view.petSizeFailed(data)
                view.hidePetSizeLoading()
            }
        })
    }

    fun deletePetSize(id: String){
        view.showPetSizeLoading()
        repository.deletePetSize(id, object : PetSizeRepositoryCallback<PetSizeResponse>{
            override fun petSizeSuccess(data: PetSizeResponse?) {
                view.petSizeSuccess(data)
                view.hidePetSizeLoading()
            }
            override fun petSizeFailed(data: String) {
                view.petSizeFailed(data)
                view.hidePetSizeLoading()
            }
        })
    }
}

//PET TYPE
class PetTypePresenter(private val view: PetTypeView, private val repository: Repository){

    fun getAllPetType(){
        view.showPetTypeLoading()
        repository.getAllPetType(object : PetTypeRepositoryCallback<PetTypeResponse> {
            override fun petTypeSuccess(data: PetTypeResponse?) {
                view.petTypeSuccess(data)
                view.hidePetTypeLoading()
            }
            override fun petTypeFailed(data: String) {
                view.petTypeFailed(data)
                view.hidePetTypeLoading()
            }
        })
    }

    fun getPetTypeBySearch(query: String){
        view.showPetTypeLoading()
        repository.getPetTypeBySearch(query, object : PetTypeRepositoryCallback<PetTypeResponse>{
            override fun petTypeSuccess(data: PetTypeResponse?) {
                view.petTypeSuccess(data)
                view.hidePetTypeLoading()
            }
            override fun petTypeFailed(data: String) {
                view.petTypeFailed(data)
                view.hidePetTypeLoading()
            }
        })
    }

    fun addPetType(petType: PetType){
        view.showPetTypeLoading()
        repository.addPetType(petType, object : PetTypeRepositoryCallback<PetTypeResponse> {
            override fun petTypeSuccess(data: PetTypeResponse?) {
                view.petTypeSuccess(data)
                view.hidePetTypeLoading()
            }
            override fun petTypeFailed(data: String) {
                view.petTypeFailed(data)
                view.hidePetTypeLoading()
            }
        })
    }

    fun editPetType(id: String, petType: PetType){
        view.showPetTypeLoading()
        repository.editPetType(id, petType, object : PetTypeRepositoryCallback<PetTypeResponse> {
            override fun petTypeSuccess(data: PetTypeResponse?) {
                view.petTypeSuccess(data)
                view.hidePetTypeLoading()
            }
            override fun petTypeFailed(data: String) {
                view.petTypeFailed(data)
                view.hidePetTypeLoading()
            }
        })
    }

    fun deletePetType(id: String){
        view.showPetTypeLoading()
        repository.deletePetType(id, object : PetTypeRepositoryCallback<PetTypeResponse> {
            override fun petTypeSuccess(data: PetTypeResponse?) {
                view.petTypeSuccess(data)
                view.hidePetTypeLoading()
            }
            override fun petTypeFailed(data: String) {
                view.petTypeFailed(data)
                view.hidePetTypeLoading()
            }
        })
    }
}

//SUPPLIER
class SupplierPresenter(private val view: SupplierView, private val repository: Repository){

    fun getAllSupplier(){
        view.showSupplierLoading()
        repository.getAllSupplier(object : SupplierRepositoryCallback<SupplierResponse> {
            override fun supplierSuccess(data: SupplierResponse?) {
                view.supplierSuccess(data)
                view.hideSupplierLoading()
            }
            override fun supplierFailed(data: String) {
                view.supplierFailed(data)
                view.hideSupplierLoading()
            }
        })
    }

    fun getSupplierBySearch(query: String){
        view.showSupplierLoading()
        repository.getSupplierBySearch(query, object : SupplierRepositoryCallback<SupplierResponse>{
            override fun supplierSuccess(data: SupplierResponse?) {
                view.supplierSuccess(data)
                view.hideSupplierLoading()
            }
            override fun supplierFailed(data: String) {
                view.supplierFailed(data)
                view.hideSupplierLoading()
            }
        })
    }

    fun addSupplier(supplier: Supplier){
        view.showSupplierLoading()
        repository.addSupplier(supplier, object : SupplierRepositoryCallback<SupplierResponse> {
            override fun supplierSuccess(data: SupplierResponse?) {
                view.supplierSuccess(data)
                view.hideSupplierLoading()
            }
            override fun supplierFailed(data: String) {
                view.supplierFailed(data)
                view.hideSupplierLoading()
            }
        })
    }

    fun editSupplier(id: String, supplier: Supplier){
        view.showSupplierLoading()
        repository.editSupplier(id, supplier, object : SupplierRepositoryCallback<SupplierResponse> {
            override fun supplierSuccess(data: SupplierResponse?) {
                view.supplierSuccess(data)
                view.hideSupplierLoading()
            }
            override fun supplierFailed(data: String) {
                view.supplierFailed(data)
                view.hideSupplierLoading()
            }
        })
    }

    fun deleteSupplier(id: String){
        view.showSupplierLoading()
        repository.deleteSupplier(id, object : SupplierRepositoryCallback<SupplierResponse> {
            override fun supplierSuccess(data: SupplierResponse?) {
                view.supplierSuccess(data)
                view.hideSupplierLoading()
            }
            override fun supplierFailed(data: String) {
                view.supplierFailed(data)
                view.hideSupplierLoading()
            }
        })
    }
}

//SERVICE
class ServicePresenter(private val view: ServiceView, private val repository: Repository){

    fun getAllService(){
        view.showServiceLoading()
        repository.getAllService(object : ServiceRepositoryCallback<ServiceResponse> {
            override fun serviceSuccess(data: ServiceResponse?) {
                view.serviceSuccess(data)
                view.hideServiceLoading()
            }
            override fun serviceFailed(data: String) {
                view.serviceFailed(data)
                view.hideServiceLoading()
            }
        })
    }

    fun getServiceBySearch(query: String){
        view.showServiceLoading()
        repository.getServiceBySearch(query, object : ServiceRepositoryCallback<ServiceResponse>{
            override fun serviceSuccess(data: ServiceResponse?) {
                view.serviceSuccess(data)
                view.hideServiceLoading()
            }
            override fun serviceFailed(data: String) {
                view.serviceFailed(data)
                view.hideServiceLoading()
            }
        })
    }

    fun addService(service: Service){
        view.showServiceLoading()
        repository.addService(service, object : ServiceRepositoryCallback<ServiceResponse> {
            override fun serviceSuccess(data: ServiceResponse?) {
                view.serviceSuccess(data)
                view.hideServiceLoading()
            }
            override fun serviceFailed(data: String) {
                view.serviceFailed(data)
                view.hideServiceLoading()
            }
        })
    }

    fun editService(id: String, service: Service){
        view.showServiceLoading()
        repository.editService(id, service, object : ServiceRepositoryCallback<ServiceResponse>{
            override fun serviceSuccess(data: ServiceResponse?) {
                view.serviceSuccess(data)
                view.hideServiceLoading()
            }
            override fun serviceFailed(data: String) {
                view.serviceFailed(data)
                view.hideServiceLoading()
            }
        })
    }

    fun deleteService(id: String){
        view.showServiceLoading()
        repository.deleteService(id, object : ServiceRepositoryCallback<ServiceResponse> {
            override fun serviceSuccess(data: ServiceResponse?) {
                view.serviceSuccess(data)
                view.hideServiceLoading()
            }
            override fun serviceFailed(data: String) {
                view.serviceFailed(data)
                view.hideServiceLoading()
            }
        })
    }
}

//ORDER PRODUCT
class OrderProductPresenter(private val view: OrderProductView, private val repository: Repository){

    fun getAllOrderProduct(){
        view.showOrderProductLoading()
        repository.getAllOrderProduct(object : OrderProductRepositoryCallback<OrderProductResponse>{
            override fun orderProductSuccess(data: OrderProductResponse?) {
                view.orderProductSuccess(data)
                view.hideOrderProductLoading()
            }
            override fun orderProductFailed(data: String) {
                view.orderProductFailed(data)
                view.hideOrderProductLoading()
            }
        })
    }

    fun addOrderProduct(orderProduct: OrderProduct){
        view.showOrderProductLoading()
        repository.addOrderProduct(orderProduct, object : OrderProductRepositoryCallback<OrderProductResponse>{
            override fun orderProductSuccess(data: OrderProductResponse?) {
                view.orderProductSuccess(data)
                view.hideOrderProductLoading()
            }
            override fun orderProductFailed(data: String) {
                view.orderProductFailed(data)
                view.hideOrderProductLoading()
            }
        })
    }

    fun editOrderProduct(id: String, orderProduct: OrderProduct){
        view.showOrderProductLoading()
        repository.editOrderProduct(id, orderProduct, object : OrderProductRepositoryCallback<OrderProductResponse>{
            override fun orderProductSuccess(data: OrderProductResponse?) {
                view.orderProductSuccess(data)
                view.hideOrderProductLoading()
            }
            override fun orderProductFailed(data: String) {
                view.orderProductFailed(data)
                view.hideOrderProductLoading()
            }
        })
    }

    fun deleteOrderProduct(id: String){
        view.showOrderProductLoading()
        repository.deleteOrderProduct(id, object : OrderProductRepositoryCallback<OrderProductResponse>{
            override fun orderProductSuccess(data: OrderProductResponse?) {
                view.orderProductSuccess(data)
                view.hideOrderProductLoading()
            }
            override fun orderProductFailed(data: String) {
                view.orderProductFailed(data)
                view.hideOrderProductLoading()
            }
        })
    }

    fun editTotalOrderProduct(id: String){
        view.showOrderProductLoading()
        repository.editTotalOrderProduct(id, object : OrderProductRepositoryCallback<OrderProductResponse>{
            override fun orderProductSuccess(data: OrderProductResponse?) {
                view.orderProductSuccess(data)
                view.hideOrderProductLoading()
            }
            override fun orderProductFailed(data: String) {
                view.orderProductFailed(data)
                view.hideOrderProductLoading()
            }
        })
    }

    fun editDoneOrderProduct(id: String){
        view.showOrderProductLoading()
        repository.editDoneOrderProduct(id, object : OrderProductRepositoryCallback<OrderProductResponse>{
            override fun orderProductSuccess(data: OrderProductResponse?) {
                view.orderProductSuccess(data)
                view.hideOrderProductLoading()
            }
            override fun orderProductFailed(data: String) {
                view.orderProductFailed(data)
                view.hideOrderProductLoading()
            }
        })
    }

    fun editPrintOrderProduct(id: String){
        view.showOrderProductLoading()
        repository.editPrintOrderProduct(id, object : OrderProductRepositoryCallback<OrderProductResponse>{
            override fun orderProductSuccess(data: OrderProductResponse?) {
                view.orderProductSuccess(data)
                view.hideOrderProductLoading()
            }
            override fun orderProductFailed(data: String) {
                view.orderProductFailed(data)
                view.hideOrderProductLoading()
            }
        })
    }
}

//DETAIL ORDER PRODUCT
class DetailOrderProductPresenter(private val view: DetailOrderProductView, private val repository: Repository){

    fun getAllDetailOrderProduct(){
        view.showDetailOrderProductLoading()
        repository.getAllDetailOrderProduct(object : DetailOrderProductRepositoryCallback<DetailOrderProductResponse>{
            override fun detailOrderProductSuccess(data: DetailOrderProductResponse?) {
                view.detailOrderProductSuccess(data)
                view.hideDetailOrderProductLoading()
            }
            override fun detailOrderProductFailed(data: String) {
                view.detailOrderProductFailed(data)
                view.hideDetailOrderProductLoading()
            }
        })
    }

    fun getDetailOrderProductByOrderId(id: String){
        view.showDetailOrderProductLoading()
        repository.getDetailOrderProductByOrderId(id, object : DetailOrderProductRepositoryCallback<DetailOrderProductResponse>{
            override fun detailOrderProductSuccess(data: DetailOrderProductResponse?) {
                view.detailOrderProductSuccess(data)
                view.hideDetailOrderProductLoading()
            }
            override fun detailOrderProductFailed(data: String) {
                view.detailOrderProductFailed(data)
                view.hideDetailOrderProductLoading()
            }
        })
    }

    fun addDetailOrderProduct(detailOrderProduct: DetailOrderProduct){
        view.showDetailOrderProductLoading()
        repository.addDetailOrderProduct(detailOrderProduct, object : DetailOrderProductRepositoryCallback<DetailOrderProductResponse>{
            override fun detailOrderProductSuccess(data: DetailOrderProductResponse?) {
                view.detailOrderProductSuccess(data)
                view.hideDetailOrderProductLoading()
            }
            override fun detailOrderProductFailed(data: String) {
                view.detailOrderProductFailed(data)
                view.hideDetailOrderProductLoading()
            }
        })
    }

    fun editDetailOrderProduct(detailOrderProduct: DetailOrderProduct){
        view.showDetailOrderProductLoading()
        repository.editDetailOrderProduct(detailOrderProduct, object : DetailOrderProductRepositoryCallback<DetailOrderProductResponse>{
            override fun detailOrderProductSuccess(data: DetailOrderProductResponse?) {
                view.detailOrderProductSuccess(data)
                view.hideDetailOrderProductLoading()
            }
            override fun detailOrderProductFailed(data: String) {
                view.detailOrderProductFailed(data)
                view.hideDetailOrderProductLoading()
            }
        })
    }

    fun deleteDetailOrderProduct(id_order: String, id_product: String){
        view.showDetailOrderProductLoading()
        repository.deleteDetailOrderProduct(id_order, id_product, object : DetailOrderProductRepositoryCallback<DetailOrderProductResponse>{
            override fun detailOrderProductSuccess(data: DetailOrderProductResponse?) {
                view.detailOrderProductSuccess(data)
                view.hideDetailOrderProductLoading()
            }
            override fun detailOrderProductFailed(data: String) {
                view.detailOrderProductFailed(data)
                view.hideDetailOrderProductLoading()
            }
        })
    }
}

//CUSTOMER PET
class CustomerPetPresenter(private val view: CustomerPetView, private val repository: Repository){
    fun getAllCustomerPet(){
        view.showCustomerPetLoading()
        repository.getAllCustomerPet(object : CustomerPetRepositoryCallback<CustomerPetResponse> {
            override fun customerPetSuccess(data: CustomerPetResponse?) {
                view.customerPetSuccess(data)
                view.hideCustomerPetLoading()
            }
            override fun customerPetFailed(data: String) {
                view.customerPetFailed(data)
                view.hideCustomerPetLoading()
            }
        })
    }

    fun getCustomerPetBySearch(query: String){
        view.showCustomerPetLoading()
        repository.getCustomerPetBySearch(query, object : CustomerPetRepositoryCallback<CustomerPetResponse>{
            override fun customerPetSuccess(data: CustomerPetResponse?) {
                view.customerPetSuccess(data)
                view.hideCustomerPetLoading()
            }
            override fun customerPetFailed(data: String) {
                view.customerPetFailed(data)
                view.hideCustomerPetLoading()
            }
        })
    }

    fun addCustomerPet(customerPet: CustomerPet){
        view.showCustomerPetLoading()
        repository.addCustomerPet(customerPet, object : CustomerPetRepositoryCallback<CustomerPetResponse>{
            override fun customerPetSuccess(data: CustomerPetResponse?) {
                view.customerPetSuccess(data)
                view.hideCustomerPetLoading()
            }
            override fun customerPetFailed(data: String) {
                view.customerPetFailed(data)
                view.hideCustomerPetLoading()
            }
        })
    }

    fun editCustomerPet(id: String, customerPet: CustomerPet){
        view.showCustomerPetLoading()
        repository.editCustomerPet(id, customerPet, object : CustomerPetRepositoryCallback<CustomerPetResponse>{
            override fun customerPetSuccess(data: CustomerPetResponse?) {
                view.customerPetSuccess(data)
                view.hideCustomerPetLoading()
            }
            override fun customerPetFailed(data: String) {
                view.customerPetFailed(data)
                view.hideCustomerPetLoading()
            }
        })
    }

    fun deleteCustomerPet(id: String){
        view.showCustomerPetLoading()
        repository.deleteCustomerPet(id, object : CustomerPetRepositoryCallback<CustomerPetResponse>{
            override fun customerPetSuccess(data: CustomerPetResponse?) {
                view.customerPetSuccess(data)
                view.hideCustomerPetLoading()
            }
            override fun customerPetFailed(data: String) {
                view.customerPetFailed(data)
                view.hideCustomerPetLoading()
            }
        })
    }
}

//TRANSACTION
class TransactionPresenter(private val view: TransactionView, private val repository: Repository){

    fun getAllTransaction(){
        view.showTransactionLoading()
        repository.getAllTransaction(object : TransactionRepositoryCallback<TransactionResponse>{
            override fun transactionSuccess(data: TransactionResponse?) {
                view.transactionSuccess(data)
                view.hideTransactionLoading()
            }
            override fun transactionFailed(data: String) {
                view.transactionFailed(data)
                view.hideTransactionLoading()
            }
        })
    }

    fun getAllProductTransaction(){
        view.showTransactionLoading()
        repository.getAllProductTransaction(object : TransactionRepositoryCallback<TransactionResponse>{
            override fun transactionSuccess(data: TransactionResponse?) {
                view.transactionSuccess(data)
                view.hideTransactionLoading()
            }
            override fun transactionFailed(data: String) {
                view.transactionFailed(data)
                view.hideTransactionLoading()
            }
        })
    }

    fun getAllServiceTransaction(){
        view.showTransactionLoading()
        repository.getAllServiceTransaction(object : TransactionRepositoryCallback<TransactionResponse>{
            override fun transactionSuccess(data: TransactionResponse?) {
                view.transactionSuccess(data)
                view.hideTransactionLoading()
            }
            override fun transactionFailed(data: String) {
                view.transactionFailed(data)
                view.hideTransactionLoading()
            }
        })
    }

    fun addTransaction(type: String, transaction: Transaction){
        view.showTransactionLoading()
        repository.addTransaction(type, transaction, object : TransactionRepositoryCallback<TransactionResponse>{
            override fun transactionSuccess(data: TransactionResponse?) {
                view.transactionSuccess(data)
                view.hideTransactionLoading()
            }
            override fun transactionFailed(data: String) {
                view.transactionFailed(data)
                view.hideTransactionLoading()
            }
        })
    }

    fun editTotalTransaction(id: String){
        view.showTransactionLoading()
        repository.editTotalTransaction(id, object : TransactionRepositoryCallback<TransactionResponse>{
            override fun transactionSuccess(data: TransactionResponse?) {
                view.transactionSuccess(data)
                view.hideTransactionLoading()
            }
            override fun transactionFailed(data: String) {
                view.transactionFailed(data)
                view.hideTransactionLoading()
            }
        })
    }

    fun editPetTransaction(id: String, transaction: Transaction){
        view.showTransactionLoading()
        repository.editPetTransaction(id, transaction, object : TransactionRepositoryCallback<TransactionResponse>{
            override fun transactionSuccess(data: TransactionResponse?) {
                view.transactionSuccess(data)
                view.hideTransactionLoading()
            }
            override fun transactionFailed(data: String) {
                view.transactionFailed(data)
                view.hideTransactionLoading()
            }
        })
    }

    fun editDoneTransaction(id: String, transaction: Transaction){
        view.showTransactionLoading()
        repository.editDoneTransaction(id, transaction, object : TransactionRepositoryCallback<TransactionResponse>{
            override fun transactionSuccess(data: TransactionResponse?) {
                view.transactionSuccess(data)
                view.hideTransactionLoading()
            }
            override fun transactionFailed(data: String) {
                view.transactionFailed(data)
                view.hideTransactionLoading()
            }
        })
    }

    fun editStatusTransaction(id: String, transaction: Transaction){
        view.showTransactionLoading()
        repository.editStatusTransaction(id, transaction, object : TransactionRepositoryCallback<TransactionResponse>{
            override fun transactionSuccess(data: TransactionResponse?) {
                view.transactionSuccess(data)
                view.hideTransactionLoading()
            }
            override fun transactionFailed(data: String) {
                view.transactionFailed(data)
                view.hideTransactionLoading()
            }
        })
    }

    fun cancelTransaction(id: String){
        view.showTransactionLoading()
        repository.cancelTransaction(id, object : TransactionRepositoryCallback<TransactionResponse>{
            override fun transactionSuccess(data: TransactionResponse?) {
                view.transactionSuccess(data)
                view.hideTransactionLoading()
            }
            override fun transactionFailed(data: String) {
                view.transactionFailed(data)
                view.hideTransactionLoading()
            }
        })
    }
}

//DETAIL PRODUCT TRANSACTION
class DetailProductTransactionPresenter(private val view: DetailProductTransactionView, private val repository: Repository){

    fun getAllDetailProductTransaction(){
        view.showDetailProductTransactionLoading()
        repository.getAllDetailProductTransaction(object : DetailProductTransactionRepositoryCallback<DetailProductTransactionResponse>{
            override fun detailProductTransactionSuccess(data: DetailProductTransactionResponse?) {
                view.detailProductTransactionSuccess(data)
                view.hideDetailProductTransactionLoading()
            }
            override fun detailProductTransactionFailed(data: String) {
                view.detailProductTransactionFailed(data)
                view.hideDetailProductTransactionLoading()
            }
        })
    }

    fun addDetailProductTransaction(detailProductTransaction: DetailProductTransaction){
        view.showDetailProductTransactionLoading()
        repository.addDetailProductTransaction(detailProductTransaction, object : DetailProductTransactionRepositoryCallback<DetailProductTransactionResponse>{
            override fun detailProductTransactionSuccess(data: DetailProductTransactionResponse?) {
                view.detailProductTransactionSuccess(data)
                view.hideDetailProductTransactionLoading()
            }
            override fun detailProductTransactionFailed(data: String) {
                view.detailProductTransactionFailed(data)
                view.hideDetailProductTransactionLoading()
            }
        })
    }

    fun editDetailProductTransaction(detailProductTransaction: DetailProductTransaction){
        view.showDetailProductTransactionLoading()
        repository.editDetailProductTransaction(detailProductTransaction, object : DetailProductTransactionRepositoryCallback<DetailProductTransactionResponse>{
            override fun detailProductTransactionSuccess(data: DetailProductTransactionResponse?) {
                view.detailProductTransactionSuccess(data)
                view.hideDetailProductTransactionLoading()
            }
            override fun detailProductTransactionFailed(data: String) {
                view.detailProductTransactionFailed(data)
                view.hideDetailProductTransactionLoading()
            }
        })
    }

    fun deleteDetailProductTransaction(id_transaction: String, id_product: String){
        view.showDetailProductTransactionLoading()
        repository.deleteDetailProductTransaction(id_transaction, id_product, object : DetailProductTransactionRepositoryCallback<DetailProductTransactionResponse>{
            override fun detailProductTransactionSuccess(data: DetailProductTransactionResponse?) {
                view.detailProductTransactionSuccess(data)
                view.hideDetailProductTransactionLoading()
            }
            override fun detailProductTransactionFailed(data: String) {
                view.detailProductTransactionFailed(data)
                view.hideDetailProductTransactionLoading()
            }
        })
    }

    fun getDetailProductTransactionByIdTransaction(id_transaction: String){
        view.showDetailProductTransactionLoading()
        repository.getDetailProductTransactionByIdTransaction(id_transaction, object : DetailProductTransactionRepositoryCallback<DetailProductTransactionResponse>{
            override fun detailProductTransactionSuccess(data: DetailProductTransactionResponse?) {
                view.detailProductTransactionSuccess(data)
                view.hideDetailProductTransactionLoading()
            }
            override fun detailProductTransactionFailed(data: String) {
                view.detailProductTransactionFailed(data)
                view.hideDetailProductTransactionLoading()
            }
        })
    }

    fun deleteAllDetailProductTransaction(id_transaction: String){
        view.showDetailProductTransactionLoading()
        repository.deleteAllDetailProductTransaction(id_transaction, object : DetailProductTransactionRepositoryCallback<DetailProductTransactionResponse>{
            override fun detailProductTransactionSuccess(data: DetailProductTransactionResponse?) {
                view.detailProductTransactionSuccess(data)
                view.hideDetailProductTransactionLoading()
            }
            override fun detailProductTransactionFailed(data: String) {
                view.detailProductTransactionFailed(data)
                view.hideDetailProductTransactionLoading()
            }
        })
    }
}

//DETAIL SERVICE TRANSACTION
class DetailServiceTransactionPresenter(private val view: DetailServiceTransactionView, private val repository: Repository){
    fun getAllDetailServiceTransaction(){
        view.showDetailServiceTransactionLoading()
        repository.getAllDetailServiceTransaction(object : DetailServiceTransactionRepositoryCallback<DetailServiceTransactionResponse>{
            override fun detailServiceTransactionSuccess(data: DetailServiceTransactionResponse?) {
                view.detailServiceTransactionSuccess(data)
                view.hideDetailServiceTransactionLoading()
            }
            override fun detailServiceTransactionFailed(data: String) {
                view.detailServiceTransactionFailed(data)
                view.hideDetailServiceTransactionLoading()
            }
        })
    }

    fun addDetailServiceTransaction(detailServiceTransaction: DetailServiceTransaction){
        view.showDetailServiceTransactionLoading()
        repository.addDetailServiceTransaction(detailServiceTransaction, object : DetailServiceTransactionRepositoryCallback<DetailServiceTransactionResponse>{
            override fun detailServiceTransactionSuccess(data: DetailServiceTransactionResponse?) {
                view.detailServiceTransactionSuccess(data)
                view.hideDetailServiceTransactionLoading()
            }
            override fun detailServiceTransactionFailed(data: String) {
                view.detailServiceTransactionFailed(data)
                view.hideDetailServiceTransactionLoading()
            }
        })
    }

    fun editDetailServiceTransaction(detailServiceTransaction: DetailServiceTransaction){
        view.showDetailServiceTransactionLoading()
        repository.editDetailServiceTransaction(detailServiceTransaction, object : DetailServiceTransactionRepositoryCallback<DetailServiceTransactionResponse>{
            override fun detailServiceTransactionSuccess(data: DetailServiceTransactionResponse?) {
                view.detailServiceTransactionSuccess(data)
                view.hideDetailServiceTransactionLoading()
            }
            override fun detailServiceTransactionFailed(data: String) {
                view.detailServiceTransactionFailed(data)
                view.hideDetailServiceTransactionLoading()
            }
        })
    }

    fun deleteDetailServiceTransaction(id_transaction: String, id_service: String){
        view.showDetailServiceTransactionLoading()
        repository.deleteDetailServiceTransaction(id_transaction, id_service, object : DetailServiceTransactionRepositoryCallback<DetailServiceTransactionResponse>{
            override fun detailServiceTransactionSuccess(data: DetailServiceTransactionResponse?) {
                view.detailServiceTransactionSuccess(data)
                view.hideDetailServiceTransactionLoading()
            }
            override fun detailServiceTransactionFailed(data: String) {
                view.detailServiceTransactionFailed(data)
                view.hideDetailServiceTransactionLoading()
            }
        })
    }

    fun getDetailServiceTransactionByIdTransaction(id_transaction: String){
        view.showDetailServiceTransactionLoading()
        repository.getDetailServiceTransactionByIdTransaction(id_transaction, object : DetailServiceTransactionRepositoryCallback<DetailServiceTransactionResponse>{
            override fun detailServiceTransactionSuccess(data: DetailServiceTransactionResponse?) {
                view.detailServiceTransactionSuccess(data)
                view.hideDetailServiceTransactionLoading()
            }
            override fun detailServiceTransactionFailed(data: String) {
                view.detailServiceTransactionFailed(data)
                view.hideDetailServiceTransactionLoading()
            }
        })
    }

    fun deleteAllDetailServiceTransaction(id_transaction: String){
        view.showDetailServiceTransactionLoading()
        repository.deleteAllDetailServiceTransaction(id_transaction, object : DetailServiceTransactionRepositoryCallback<DetailServiceTransactionResponse>{
            override fun detailServiceTransactionSuccess(data: DetailServiceTransactionResponse?) {
                view.detailServiceTransactionSuccess(data)
                view.hideDetailServiceTransactionLoading()
            }
            override fun detailServiceTransactionFailed(data: String) {
                view.detailServiceTransactionFailed(data)
                view.hideDetailServiceTransactionLoading()
            }
        })
    }
}

class MinProductPresenter(private val view: MinProductView, private val repository: Repository){
    fun getMinProduct(){
        view.showMinProductLoading()
        repository.getMinProduct(object : MinProductRepositoryCallback<ProductResponse>{
            override fun minProductSuccess(data: ProductResponse?) {
                view.minProductSuccess(data)
                view.hideMinProductLoading()
            }
            override fun minProductFailed(data: String) {
                view.minProductFailed(data)
                view.hideMinProductLoading()
            }
        })
    }
}

class OrderInvoicePresenter(private val view: OrderInvoiceView, private val repository: Repository){
    fun getOrderInvoice(id_order: String){
        view.showDownloadProgress()
        repository.downloadOrderInvoice(id_order, object : OrderInvoiceRepositoryCallback<ResponseBody>{
            override fun orderInvoiceSuccess(data: ResponseBody?) {
                view.orderInvoiceSuccess(data)
                view.hideDownloadProgress()
            }
            override fun orderInvoiceFailed(data: String) {
                view.orderInvoiceFailed(data)
                view.hideDownloadProgress()
            }
        })
    }
}
