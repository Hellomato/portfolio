

//------------------------------------------------------------------------------
// <auto-generated>
//    This code was generated from a template.
//
//    Manual changes to this file may cause unexpected behavior in your application.
//    Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------


namespace WorkingAPI.Models
{

using System;
    using System.Collections.Generic;
    
public partial class EXPIRED_TICKETS
{

    public string TICKET_NO { get; set; }

    public Nullable<decimal> CUSTOMER_ID { get; set; }

    public Nullable<decimal> JOURNEY_ID { get; set; }

    public Nullable<decimal> PRICE { get; set; }

    public CUSTOMER CUSTOMER { get; set; }

    public EXPIRED_JOURNEYS EXPIRED_JOURNEYS { get; set; }

}

}