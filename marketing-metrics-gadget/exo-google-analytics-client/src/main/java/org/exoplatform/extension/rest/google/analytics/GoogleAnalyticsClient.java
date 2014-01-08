/*
 * Copyright (C) 2003-2013 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU Affero General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.extension.rest.google.analytics;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.RuntimeDelegate;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.impl.RuntimeDelegateImpl;
import org.exoplatform.services.rest.resource.ResourceContainer;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.AnalyticsScopes;
import com.google.api.services.analytics.model.GaData;

/**
 * Created by The eXo Platform SAS
 * Dec 30, 2013  
 */
@Path("/ga")
public class GoogleAnalyticsClient implements ResourceContainer{
  
  private static final Log log = ExoLogger.getLogger(GoogleAnalyticsClient.class);
  private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
  private final JsonFactory JSON_FACTORY = new JacksonFactory();
  private static final CacheControl cacheControl;
  
  
  static {
    RuntimeDelegate.setInstance(new RuntimeDelegateImpl());
    cacheControl = new CacheControl();
  }
  
  @GET
  @Path("get-data/{account_id}/{view_id}")
  public Response main(@Context SecurityContext sc,
                       @Context UriInfo uriInfo,
                       @PathParam("account_id") String account_id,
                       @PathParam("view_id") String view_id,
                       @QueryParam("start_date") String start_date, 
                       @QueryParam("end_date") String end_date) {    
    
     GoogleCredential credential = null;
     try {
          credential = new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT)
                .setJsonFactory(JSON_FACTORY)
                .setServiceAccountId(account_id)
                .setServiceAccountScopes(Collections.singleton(AnalyticsScopes.ANALYTICS_READONLY))
                .setServiceAccountPrivateKeyFromP12File(new File("ga_privatekey.p12"))                          
                .build();
       } catch (GeneralSecurityException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();  
       }
       
       // Set up and return Google Analytics API client.
       Analytics analytics = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName("Analytics").build();
       

       GaData gaData = null;
       try {
          gaData = executeDataQuery(analytics, view_id, start_date, end_date);
       } catch (IOException e) {
          e.printStackTrace();
       }       
       gaData.setFactory(JSON_FACTORY);
       return Response.ok(gaData,MediaType.APPLICATION_JSON
                          ).build();
  }

  /**
   * Returns the top 25 organic search keywords and traffic source by visits. The Core Reporting API
   * is used to retrieve this data.
   *
   * @param analytics the analytics service object used to access the API.
   * @param profileId the profile ID from which to retrieve data.
   * @return the response from the API.
   * @throws IOException tf an API error occured.
   */
  private GaData executeDataQuery(Analytics analytics, String profileId, String start_date, String end_date) throws IOException {
      return analytics.data().ga().get("ga:" + profileId, // Table Id. ga: + profile id.
              start_date,
              end_date,
              "ga:visits,ga:pageviews") // Metrics.
              .setDimensions("ga:date")
              .setSort("ga:date")              
              .execute();
  }
}
