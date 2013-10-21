﻿using System;
using System.Data;
using System.Data.SqlClient;
using System.Security.Cryptography;
using System.Text;

namespace RepoLibrary
{
    class MSSQLDatabaseGateway : IDatabaseGateway
    {

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

        public MSSQLDatabaseGateway(string connectionString)
        {
            this.connectionString = connectionString;
        }

        public Project GetProjectByKey(string key)
        {
            SqlConnection connection = new SqlConnection(this.connectionString);
            connection.Open();
            SqlDataReader myReader = null;
            SqlCommand myCommand = new SqlCommand("select * from shops where shopKeyHash=@key", connection);
            myCommand.Parameters.Add("@key", SqlDbType.VarChar).Value = this.CreateMD5Hash(key);
            myReader = myCommand.ExecuteReader();
            while (myReader.Read())
            {
                Project project = new Project();
                project.Settings = myReader["shopSettings"].ToString();
                project.Url = myReader["shopUrl"].ToString();
                project.ExpirationDate = myReader["shopExpirationDate"].ToString();
                project.apiUrl = myReader["shopApiUrl"].ToString();
                project.keyHash = myReader["shopKeyHash"].ToString();
                connection.Close();
                return project;
            }
            connection.Close();
            return null;
        }

        public Project GetProject(string url)
        {
            SqlConnection connection = new SqlConnection(this.connectionString);
            connection.Open();
            SqlDataReader myReader = null;
            SqlCommand myCommand = new SqlCommand("select * from shops where shopUrl=@url", connection);
            myCommand.Parameters.Add("@url", SqlDbType.VarChar).Value = url;
            myReader = myCommand.ExecuteReader();
            while (myReader.Read())
            {
                Project project = new Project();
                project.Settings = myReader["shopSettings"].ToString();
                project.Url = myReader["shopUrl"].ToString();
                project.ExpirationDate = myReader["shopExpirationDate"].ToString();
                project.apiUrl = myReader["shopApiUrl"].ToString();
                project.keyHash = myReader["shopKeyHash"].ToString();
                connection.Close();
                return project;
            }
            connection.Close();
            return null;
        }

        public string SaveProject(Project data)
        {
            Project project = GetProject(data.Url);
            if (project == null)
            {
                SqlConnection connection = new SqlConnection(this.connectionString);
                connection.Open();
                SqlCommand myCommand = new SqlCommand("INSERT INTO shops (shopUrl, shopSettings, shopExpirationDate, shopApiUrl, shopKeyHash) " +
                    "Values (@url,@settings,@expiration,@apiUrl,@keyHash)", connection);
                myCommand.Parameters.Add("@url", SqlDbType.VarChar).Value = data.Url;
                myCommand.Parameters.Add("@settings", SqlDbType.VarChar).Value = data.Settings;
                myCommand.Parameters.Add("@expiration", SqlDbType.VarChar).Value = data.ExpirationDate;
                myCommand.Parameters.Add("@apiUrl", SqlDbType.VarChar).Value = data.apiUrl;
                myCommand.Parameters.Add("@keyHash", SqlDbType.VarChar).Value = data.keyHash;
                int result = myCommand.ExecuteNonQuery();
                connection.Close();
                if (result > 0)
                {
                    return "ok";
                }
                else
                {
                    return "Failed to add project";
                }
            }
            else
            {
                SqlConnection connection = new SqlConnection(this.connectionString);
                connection.Open();
                SqlCommand myCommand = new SqlCommand("UPDATE shops SET shopSettings=@settings, shopExpirationDate=@expiration, shopApiUrl=@apiUrl, shopKeyHash=@keyHash WHERE shopUrl=@url", connection);
                myCommand.Parameters.Add("@url", SqlDbType.VarChar).Value = data.Url;
                myCommand.Parameters.Add("@settings", SqlDbType.VarChar).Value = data.Settings;
                myCommand.Parameters.Add("@expiration", SqlDbType.VarChar).Value = data.ExpirationDate;
                myCommand.Parameters.Add("@apiUrl", SqlDbType.VarChar).Value = data.apiUrl;
                myCommand.Parameters.Add("@keyHash", SqlDbType.VarChar).Value = data.keyHash;
                int result = myCommand.ExecuteNonQuery();
                connection.Close();
                if (result > 0)
                {
                    return "ok";
                }
                else
                {
                    return "Failed to add project";
                }
            }
        }
        private string connectionString;
    }
}