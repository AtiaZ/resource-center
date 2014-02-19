import org.opensocial.providers.Provider
import org.opensocial.auth.OAuth2LeggedScheme
import org.opensocial.Client
import org.opensocial.services.ActivitiesService
import org.opensocial.Request
import org.opensocial.models.Activity

class NotificationService {

    boolean transactional = true

    private def getOpenSocialClient() {
      //We configure our provider with the information of our eXo Social server
      def provider = new Provider();
      provider.name = "eXoSocial"
      provider.rpcEndpoint = "http://localhost:8080/social/rpc"

      //we prepare the authentication schema
      def scheme = new OAuth2LeggedScheme("myGrailsBookFlowKey", "mySuperConsumerSecret", "organization:root")

      return new Client(provider, scheme)
    }

    def notifyPurchase(Order o) {
      def client = getOpenSocialClient()

      //we create our new activity
      Activity activity = new Activity()
      def books = []
      o.items.each {
        books << "<b>${it.title}</b>"
      }
      activity.title = o.person.name + " purchased " + books.join(", ")

      //We prepare the request that will create the activity
      Request request = ActivitiesService.createActivity(activity);
      //We specify that the creation of this new activity is for the space bookflow
      request.groupId = "space:bookflow";
			client.send(request);  
       
    }
}
