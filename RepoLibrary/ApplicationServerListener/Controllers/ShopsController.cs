﻿using System;
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
                string expirationDate = project.ExpirationDate;
                DateTime todate = DateTime.Today;
                DateTime expiring;
                bool parsed = DateTime.TryParse(expirationDate, out expiring);
                if (parsed)
                {
                    if (DateTime.Compare(todate, expiring) > 0)
                    {
                        return new HttpResponseMessage() { Content = new StringContent("{\"subscribed\": \"expired\"}") };
                    }
                    else
                    {
                        return new HttpResponseMessage() { Content = new StringContent("{\"subscribed\": \"active\"}") };
                    }
                }
                else
                {
                    return new HttpResponseMessage() { Content = new StringContent("{\"subscribed\": \"none\"}") };
                }
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
                                if (project.firstExpiredLogin == "Never")
                                {
                                    project.firstExpiredLogin = todate.ToString();
                                    wcfClient.SaveProject(project);
                                }
                                DateTime expiredLogin = DateTime.Parse(project.firstExpiredLogin);
                                int daysLeft = 5 - (todate - expiredLogin).Days;
                                if (daysLeft > 0)
                                {
                                    return new HttpResponseMessage() { Content = new StringContent("{\"api\": \"expiring\", \"remains\": \"" + daysLeft.ToString() + "\",  \"url\": \"" + project.apiUrl + "\" }") };
                                }
                                else
                                {
                                    return new HttpResponseMessage() { Content = new StringContent("{\"api\": \"expired\"}") };
                                }
                            }
                            else
                            {
                                project.firstExpiredLogin = "Never";
                                wcfClient.SaveProject(project);
                                return new HttpResponseMessage() { Content = new StringContent("{\"api\": \"" + project.apiUrl + "\"}") };
                            }
                        }
                        else
                        {
                            return new HttpResponseMessage() { Content = new StringContent("{\"api\": \"notSubscribed\"}") };
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