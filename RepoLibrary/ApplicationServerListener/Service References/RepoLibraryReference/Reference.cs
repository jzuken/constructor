﻿//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//     Runtime Version:4.0.30319.34003
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace ApplicationServerListener.RepoLibraryReference {
    using System.Runtime.Serialization;
    using System;
    
    
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Runtime.Serialization", "4.0.0.0")]
    [System.Runtime.Serialization.DataContractAttribute(Name="Project", Namespace="http://schemas.datacontract.org/2004/07/RepoLibrary")]
    [System.SerializableAttribute()]
    public partial class Project : object, System.Runtime.Serialization.IExtensibleDataObject, System.ComponentModel.INotifyPropertyChanged {
        
        [System.NonSerializedAttribute()]
        private System.Runtime.Serialization.ExtensionDataObject extensionDataField;
        
        [System.Runtime.Serialization.OptionalFieldAttribute()]
        private string ExpirationDateField;
        
        [System.Runtime.Serialization.OptionalFieldAttribute()]
        private string SettingsField;
        
        [System.Runtime.Serialization.OptionalFieldAttribute()]
        private string UrlField;
        
        [System.Runtime.Serialization.OptionalFieldAttribute()]
        private string apiUrlField;
        
        [System.Runtime.Serialization.OptionalFieldAttribute()]
        private string firstExpiredLoginField;
        
        [System.Runtime.Serialization.OptionalFieldAttribute()]
        private string keyHashField;
        
        [global::System.ComponentModel.BrowsableAttribute(false)]
        public System.Runtime.Serialization.ExtensionDataObject ExtensionData {
            get {
                return this.extensionDataField;
            }
            set {
                this.extensionDataField = value;
            }
        }
        
        [System.Runtime.Serialization.DataMemberAttribute()]
        public string ExpirationDate {
            get {
                return this.ExpirationDateField;
            }
            set {
                if ((object.ReferenceEquals(this.ExpirationDateField, value) != true)) {
                    this.ExpirationDateField = value;
                    this.RaisePropertyChanged("ExpirationDate");
                }
            }
        }
        
        [System.Runtime.Serialization.DataMemberAttribute()]
        public string Settings {
            get {
                return this.SettingsField;
            }
            set {
                if ((object.ReferenceEquals(this.SettingsField, value) != true)) {
                    this.SettingsField = value;
                    this.RaisePropertyChanged("Settings");
                }
            }
        }
        
        [System.Runtime.Serialization.DataMemberAttribute()]
        public string Url {
            get {
                return this.UrlField;
            }
            set {
                if ((object.ReferenceEquals(this.UrlField, value) != true)) {
                    this.UrlField = value;
                    this.RaisePropertyChanged("Url");
                }
            }
        }
        
        [System.Runtime.Serialization.DataMemberAttribute()]
        public string apiUrl {
            get {
                return this.apiUrlField;
            }
            set {
                if ((object.ReferenceEquals(this.apiUrlField, value) != true)) {
                    this.apiUrlField = value;
                    this.RaisePropertyChanged("apiUrl");
                }
            }
        }
        
        [System.Runtime.Serialization.DataMemberAttribute()]
        public string firstExpiredLogin {
            get {
                return this.firstExpiredLoginField;
            }
            set {
                if ((object.ReferenceEquals(this.firstExpiredLoginField, value) != true)) {
                    this.firstExpiredLoginField = value;
                    this.RaisePropertyChanged("firstExpiredLogin");
                }
            }
        }
        
        [System.Runtime.Serialization.DataMemberAttribute()]
        public string keyHash {
            get {
                return this.keyHashField;
            }
            set {
                if ((object.ReferenceEquals(this.keyHashField, value) != true)) {
                    this.keyHashField = value;
                    this.RaisePropertyChanged("keyHash");
                }
            }
        }
        
        public event System.ComponentModel.PropertyChangedEventHandler PropertyChanged;
        
        protected void RaisePropertyChanged(string propertyName) {
            System.ComponentModel.PropertyChangedEventHandler propertyChanged = this.PropertyChanged;
            if ((propertyChanged != null)) {
                propertyChanged(this, new System.ComponentModel.PropertyChangedEventArgs(propertyName));
            }
        }
    }
    
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
    [System.ServiceModel.ServiceContractAttribute(ConfigurationName="RepoLibraryReference.IRepoLibrary")]
    public interface IRepoLibrary {
        
        [System.ServiceModel.OperationContractAttribute(Action="http://tempuri.org/IRepoLibrary/GetProject", ReplyAction="http://tempuri.org/IRepoLibrary/GetProjectResponse")]
        ApplicationServerListener.RepoLibraryReference.Project GetProject(string url);
        
        [System.ServiceModel.OperationContractAttribute(Action="http://tempuri.org/IRepoLibrary/GetProject", ReplyAction="http://tempuri.org/IRepoLibrary/GetProjectResponse")]
        System.Threading.Tasks.Task<ApplicationServerListener.RepoLibraryReference.Project> GetProjectAsync(string url);
        
        [System.ServiceModel.OperationContractAttribute(Action="http://tempuri.org/IRepoLibrary/SaveProject", ReplyAction="http://tempuri.org/IRepoLibrary/SaveProjectResponse")]
        string SaveProject(ApplicationServerListener.RepoLibraryReference.Project data);
        
        [System.ServiceModel.OperationContractAttribute(Action="http://tempuri.org/IRepoLibrary/SaveProject", ReplyAction="http://tempuri.org/IRepoLibrary/SaveProjectResponse")]
        System.Threading.Tasks.Task<string> SaveProjectAsync(ApplicationServerListener.RepoLibraryReference.Project data);
    }
    
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
    public interface IRepoLibraryChannel : ApplicationServerListener.RepoLibraryReference.IRepoLibrary, System.ServiceModel.IClientChannel {
    }
    
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
    public partial class RepoLibraryClient : System.ServiceModel.ClientBase<ApplicationServerListener.RepoLibraryReference.IRepoLibrary>, ApplicationServerListener.RepoLibraryReference.IRepoLibrary {
        
        public RepoLibraryClient() {
        }
        
        public RepoLibraryClient(string endpointConfigurationName) : 
                base(endpointConfigurationName) {
        }
        
        public RepoLibraryClient(string endpointConfigurationName, string remoteAddress) : 
                base(endpointConfigurationName, remoteAddress) {
        }
        
        public RepoLibraryClient(string endpointConfigurationName, System.ServiceModel.EndpointAddress remoteAddress) : 
                base(endpointConfigurationName, remoteAddress) {
        }
        
        public RepoLibraryClient(System.ServiceModel.Channels.Binding binding, System.ServiceModel.EndpointAddress remoteAddress) : 
                base(binding, remoteAddress) {
        }
        
        public ApplicationServerListener.RepoLibraryReference.Project GetProject(string url) {
            return base.Channel.GetProject(url);
        }
        
        public System.Threading.Tasks.Task<ApplicationServerListener.RepoLibraryReference.Project> GetProjectAsync(string url) {
            return base.Channel.GetProjectAsync(url);
        }
        
        public string SaveProject(ApplicationServerListener.RepoLibraryReference.Project data) {
            return base.Channel.SaveProject(data);
        }
        
        public System.Threading.Tasks.Task<string> SaveProjectAsync(ApplicationServerListener.RepoLibraryReference.Project data) {
            return base.Channel.SaveProjectAsync(data);
        }
    }
}
