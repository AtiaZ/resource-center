class Order implements Serializable {    
	static belongsTo = [person:Person]      
	static hasMany = [items:Book]
	
	String invoiceNumber
	Address shippingAddress
	PaymentDetails paymentDetails        

	static embedded = ['shippingAddress', 'paymentDetails']

    static mapping = {
      //Order is a reserved word in SQL, so we specify another table name
       table 'book_order'
    }


	static constraints = {
		shippingAddress(nullable:false)
		paymentDetails(nullable:false)
	}
}