package com.example.kouveemanagement.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.kouveemanagement.CustomerServiceActivity
import com.example.kouveemanagement.MainActivity
import com.example.kouveemanagement.OwnerActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.*
import com.example.kouveemanagement.model.*
import com.example.kouveemanagement.presenter.*
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_search_result.*
import org.jetbrains.anko.startActivity

class SearchResultActivity : AppCompatActivity()
//    EmployeeView, PetTypeView, PetSizeView, SupplierView,
//    ProductView, ServiceView, CustomerView, CustomerPetView
{

    private lateinit var role: String

    //PRESENTER
    private lateinit var empPresenter: EmployeePresenter
    private lateinit var pettypePresenter: PetTypePresenter
    private lateinit var petsizePresenter: PetSizePresenter
    private lateinit var supplierPresenter: SupplierPresenter
    private lateinit var productPresenter: ProductPresenter
    private lateinit var servicePresenter: ServicePresenter
    private lateinit var customerPresenter: CustomerPresenter
    private lateinit var customerPetPresenter: CustomerPetPresenter

    //OBJECT
    private var employees: MutableList<Employee> = mutableListOf()
    private var pettypes: MutableList<PetType> = mutableListOf()
    private var petsizes: MutableList<PetSize> = mutableListOf()
    private var suppliers: MutableList<Supplier> = mutableListOf()
    private var products: MutableList<Product> = mutableListOf()
    private var services: MutableList<Service> = mutableListOf()
    private var customers: MutableList<Customer> = mutableListOf()
    private var customerPets: MutableList<CustomerPet> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        role = MainActivity.currentUser?.user_role.toString()
        val query: String? = intent.getStringExtra("query")

//        if (role == "Admin"){
//            setVisibleOwner()
//            empPresenter = EmployeePresenter(this, Repository())
//            query?.let { empPresenter.getEmployeeBySearch(it) }
//            pettypePresenter = PetTypePresenter(this, Repository())
//            query?.let { pettypePresenter.getPetTypeBySearch(it) }
//            petsizePresenter = PetSizePresenter(this, Repository())
//            query?.let { petsizePresenter.getPetSizeBySearch(it) }
//            supplierPresenter = SupplierPresenter(this, Repository())
//            query?.let { supplierPresenter.getSupplierBySearch(it) }
//            productPresenter = ProductPresenter(this, Repository())
//            query?.let { productPresenter.getProductBySearch(it) }
//            servicePresenter = ServicePresenter(this, Repository())
//            query?.let { servicePresenter.getServiceBySearch(it) }
//        }else{
//            setVisibleCustomerService()
//            customerPresenter = CustomerPresenter(this, Repository())
//            query?.let { customerPresenter.getCustomerBySearch(it) }
//            customerPetPresenter = CustomerPetPresenter(this, Repository())
//            query?.let { customerPetPresenter.getCustomerPetBySearch(it) }
//        }
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        if (role == "Admin"){
//            startActivity<OwnerActivity>()
//        }else{
//            startActivity<CustomerServiceActivity>()
//        }
//    }
//
//    override fun showEmployeeLoading() {
//        progressbar_emp.visibility = View.VISIBLE
//    }
//
//    override fun hideEmployeeLoading() {
//        progressbar_emp.visibility = View.GONE
//    }
//
//    override fun employeeSuccess(data: EmployeeResponse?) {
//        val temp: List<Employee> = data?.employees ?: emptyList()
//        if (temp.isEmpty()){
//            Toast.makeText(this, "No result", Toast.LENGTH_SHORT).show()
//        }else{
//            for (i in temp.indices){
//                employees.add(i, temp[i])
//            }
//            recyclerview_emp.adapter = EmployeeRecyclerViewAdapter(employees){
//                Toast.makeText(this, it.id, Toast.LENGTH_LONG).show()
//            }
//        }
//    }
//
//    override fun employeeFailed() {
//        Toast.makeText(this, "Employee failed", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun showPetTypeLoading() {
//        progressbar_pettype.visibility = View.VISIBLE
//    }
//
//    override fun hidePetTypeLoading() {
//        progressbar_pettype.visibility = View.GONE
//    }
//
//    override fun petTypeSuccess(data: PetTypeResponse?) {
//        val temp: List<PetType> = data?.pettype ?: emptyList()
//        if (temp.isEmpty()){
//            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
//        }else{
//            for (i in temp.indices){
//                pettypes.add(i, temp[i])
//            }
//            recyclerview_pettype.adapter = PetRecyclerViewAdapter("type", pettypes, {
//                Toast.makeText(this, it.id, Toast.LENGTH_SHORT).show()
//            }, mutableListOf(),{})
//        }
//        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun petTypeFailed() {
//        Toast.makeText(this, "Pet type failed", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun showPetSizeLoading() {
//        progressbar_petsize.visibility = View.VISIBLE
//    }
//
//    override fun hidePetSizeLoading() {
//        progressbar_petsize.visibility = View.GONE
//    }
//
//    override fun petSizeSuccess(data: PetSizeResponse?) {
//        val temp: List<PetSize> = data?.petsize ?: emptyList()
//        if (temp.isEmpty()){
//            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
//        }else{
//            for (i in temp.indices){
//                petsizes.add(i, temp[i])
//            }
//            recyclerview_petsize.adapter =
//                PetRecyclerViewAdapter("size", mutableListOf(), {}, petsizes, {
//                    Toast.makeText(this, it.id, Toast.LENGTH_LONG).show()
//                })
//        }
//    }
//
//    override fun petSizeFailed() {
//        Toast.makeText(this, "Pet size failed", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun showSupplierLoading() {
//        progressbar_supplier.visibility = View.VISIBLE
//    }
//
//    override fun hideSupplierLoading() {
//        progressbar_supplier.visibility = View.GONE
//    }
//
//    override fun supplierSuccess(data: SupplierResponse?) {
//        val temp: List<Supplier> = data?.suppliers ?: emptyList()
//        if (temp.isEmpty()){
//            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
//        }else{
//            for (i in temp.indices){
//                suppliers.add(i, temp[i])
//            }
//            recyclerview_supplier.adapter = SupplierRecyclerViewAdapter(suppliers) {
//                Toast.makeText(this, it.id, Toast.LENGTH_LONG).show()
//            }
//        }
//    }
//
//    override fun supplierFailed() {
//        Toast.makeText(this, "Supplier failed", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun showProductLoading() {
//        progressbar_product.visibility = View.VISIBLE
//    }
//
//    override fun hideProductLoading() {
//        progressbar_product.visibility = View.GONE
//    }
//
//    override fun productSuccess(data: ProductResponse?) {
//        val temp: List<Product> = data?.products ?: emptyList()
//        if (temp.isEmpty()){
//            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
//        }else{
//            for (i in temp.indices){
//                products.add(i, temp[i])
//            }
//            recyclerview_product.adapter = ProductRecyclerViewAdapter(products) {
//                Toast.makeText(this, it.id, Toast.LENGTH_LONG).show()
//            }
//        }
//    }
//
//    override fun productFailed() {
//        Toast.makeText(this, "Product failed", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun showServiceLoading() {
//        progressbar_service.visibility = View.VISIBLE
//    }
//
//    override fun hideServiceLoading() {
//        progressbar_service.visibility = View.GONE
//    }
//
//    override fun serviceSuccess(data: ServiceResponse?) {
//        val temp: List<Service> = data?.services ?: emptyList()
//        if (temp.isEmpty()){
//            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
//        }else{
//            for (i in temp.indices){
//                services.add(i, temp[i])
//            }
//            recyclerview_service.adapter = ServiceRecyclerViewAdapter(services){
//                Toast.makeText(this, it.id, Toast.LENGTH_LONG).show()
//            }
//        }
//    }
//
//    override fun serviceFailed() {
//        Toast.makeText(this, "Service failed", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun showCustomerLoading() {
//        progressbar_customer.visibility = View.VISIBLE
//    }
//
//    override fun hideCustomerLoading() {
//        progressbar_customer.visibility = View.GONE
//    }
//
//    override fun customerSuccess(data: CustomerResponse?) {
//        val temp: List<Customer> = data?.customers ?: emptyList()
//        if (temp.isEmpty()){
//            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
//        }else{
//            for (i in temp.indices){
//                customers.add(i, temp[i])
//            }
//            recyclerview_customer.adapter = CustomerRecyclerViewAdapter(customers) {
//                Toast.makeText(this, it.id, Toast.LENGTH_LONG).show()
//            }
//        }
//    }
//
//    override fun customerFailed() {
//        Toast.makeText(this, "Customer failed", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun showCustomerPetLoading() {
//        progressbar_customerpet.visibility = View.VISIBLE
//    }
//
//    override fun hideCustomerPetLoading() {
//        progressbar_customerpet.visibility = View.GONE
//    }
//
//    override fun customerPetSuccess(data: CustomerPetResponse?) {
//        val temp: List<CustomerPet> = data?.customerpets ?: emptyList()
//        if (temp.isEmpty()){
//            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
//        }else{
//            for (i in temp.indices){
//                customerPets.add(i, temp[i])
//            }
//            recyclerview_customerpet.adapter = CustomerPetRecyclerViewAdapter(customerPets){
//                Toast.makeText(this, it.id, Toast.LENGTH_LONG).show()
//            }
//        }
//    }
//
//    override fun customerPetFailed() {
//        Toast.makeText(this, "Customer pet failed", Toast.LENGTH_SHORT).show()
//    }
//
//    private fun setVisibleOwner(){
//        tv_customer.visibility = View.GONE
//        tv_customerpet.visibility = View.GONE
//    }
//
//    private fun setVisibleCustomerService(){
//        tv_emp.visibility = View.GONE
//        tv_petsize.visibility = View.GONE
//        tv_pettype.visibility = View.GONE
//        tv_product.visibility = View.GONE
//        tv_service.visibility = View.GONE
//        tv_supplier.visibility = View.GONE
//    }
}
