using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Diagnostics;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Security.Cryptography;
using System.Text;
using System.Web.Http;

namespace ApplicationServerListener.Controllers
{
    public class ShopsController : ApiController
    {
        RepoLibraryReference.RepoLibraryClient wcfClient = new RepoLibraryReference.RepoLibraryClient("WSHttpBinding_IRepoLibrary");
        
        private string CreateMD5Hash(string input)
        {
            MD5 md5 = System.Security.Cryptography.MD5.Create();
            byte[] inputBytes = System.Text.Encoding.ASCII.GetBytes(input);
            byte[] hashBytes = md5.ComputeHash(inputBytes);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < hashBytes.Length; i++)
            {
                sb.Append(hashBytes[i].ToString("X2"));
            }
            return sb.ToString();
        }
        
        // GET api/values/5
        [ActionName("DefaultAction")]
        public HttpResponseMessage Get(string name)
        {
            RepoLibraryReference.Project project = wcfClient.GetProject(@name);
            if (project == null)
            {
                return new HttpResponseMessage() {Content = new StringContent("{}") };
            }
            else
            {
                return new HttpResponseMessage() { Content = new StringContent(project.Settings) };
            }
        }

        [ActionName("CheckSubscription")]
        public HttpResponseMessage GetCheckSubscription(string name)
        {
            return this.GetCheckSubscribtion(name);
        }

        [ActionName("CheckSubscribtion")]
        public HttpResponseMessage GetCheckSubscribtion(string name)
        {
            RepoLibraryReference.Project project = wcfClient.GetProject(@name);
            if (project == null)
            {
                return new HttpResponseMessage() {Content = new StringContent("{}") };
            }
            else
            {
                //string expirationDate = project.ExpirationDate;
                DateTime todate = DateTime.Today;
                DateTime expiring = todate.AddYears(10);
                return new HttpResponseMessage() { Content = new StringContent("{\"subscribed\": \"active\", \"startDate\": \"" + todate.ToString() + 
                    "\", \"endDate\": \"" + expiring.ToString() + "\"}") };
                
                /* now all subscriptions are valid. because why not.
                bool parsed = DateTime.TryParse(expirationDate, out expiring);
                if (parsed)
                {
                    if (DateTime.Compare(todate, expiring) <= 0)
                    {
                        return new HttpResponseMessage() { Content = new StringContent("{\"subscribed\": \"active\", \"startDate\": \"" + project.subscribtionStartDate + "\", \"endDate\": \"" + project.ExpirationDate + "\"}") };
                    }
                }
                else
                {
                    DateTime trialEnd;
                    string trialEndDate = project.trialEndDate;
                    bool trialParsed = DateTime.TryParse(trialEndDate, out trialEnd);
                    if (trialParsed)
                    {
                        if (DateTime.Compare(todate, trialEnd) <= 0)
                        {
                            return new HttpResponseMessage() { Content = new StringContent("{\"subscribed\": \"trial\", \"endDate\": \"" + project.trialEndDate + "\"}") };
                        }
                    }
                }
                return new HttpResponseMessage() { Content = new StringContent("{\"subscribed\": \"expired\"}") };
                */
            }
        }

        [ActionName("ApiURL")]
        public HttpResponseMessage GetApiURL(string name)
        {
            NameValueCollection getParams = Request.RequestUri.ParseQueryString();
            if (getParams["key"] != null)
            {
                string key = getParams["key"].ToString();
                Debug.WriteLine(key);
                string hash = this.CreateMD5Hash(key);
                RepoLibraryReference.Project project = wcfClient.GetProject(@name);
                if (project == null)
                {
                    return new HttpResponseMessage() { Content = new StringContent("{\"api\": \"noShop\"}") };
                }
                else
                {
                    if (project.keyHash == hash)
                    {
                        string expirationDate = project.ExpirationDate;
                        DateTime todate = DateTime.Today;
                        DateTime expiring;
                        bool parsed = DateTime.TryParse(expirationDate, out expiring);
                        if (parsed)
                        {
                            if (DateTime.Compare(todate, expiring) > 0)
                            {
                                DateTime trialEnd;
                                string trialEndDate = project.trialEndDate;
                                bool trialParsed = DateTime.TryParse(trialEndDate, out trialEnd);
                                if (trialParsed)
                                {
                                    if (DateTime.Compare(todate, trialEnd) <= 0)
                                    {
                                        int daysTrialLeft = (trialEnd - todate).Days;
                                        return new HttpResponseMessage() { Content = new StringContent("{\"api\": \"trial\", \"remains\": \"" + daysTrialLeft.ToString() + "\",  \"url\": \"" + project.apiUrl + "\" }") };
                                    }
                                }
                                return new HttpResponseMessage() { Content = new StringContent("{\"api\": \"expired\",  \"url\": \"" + project.apiUrl + "\" }") };
                            }
                            else
                            {
                                return new HttpResponseMessage() { Content = new StringContent("{\"api\": \"ok\", \"url\": \"" + project.apiUrl + "\"}") };
                            }
                        }
                        else
                        {
                            DateTime trialEnd;
                            string trialEndDate = project.trialEndDate;
                            bool trialParsed = DateTime.TryParse(trialEndDate, out trialEnd);
                            if (trialParsed)
                            {
                                if (DateTime.Compare(todate, trialEnd) <= 0)
                                {
                                    int daysTrialLeft = (trialEnd - todate).Days;
                                    return new HttpResponseMessage() { Content = new StringContent("{\"api\": \"trial\", \"remains\": \"" + daysTrialLeft.ToString() + "\",  \"url\": \"" + project.apiUrl + "\" }") };
                                }
                            }
                            return new HttpResponseMessage() { Content = new StringContent("{\"api\": \"expired\",  \"url\": \"" + project.apiUrl + "\" }") };
                        }
                    }
                    else
                    {
                        return new HttpResponseMessage() { Content = new StringContent("{\"api\": \"wrongKey\"}") };
                    }
                }
            }
            return new HttpResponseMessage() { Content = new StringContent("{}") };
        }
    }
}