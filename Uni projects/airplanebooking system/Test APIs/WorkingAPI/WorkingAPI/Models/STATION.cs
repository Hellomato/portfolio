
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
    
public partial class STATION
{

    public STATION()
    {

        this.JOURNEYS = new HashSet<JOURNEY>();

        this.JOURNEYS1 = new HashSet<JOURNEY>();

        this.EXPIRED_JOURNEYS = new HashSet<EXPIRED_JOURNEYS>();

        this.EXPIRED_JOURNEYS1 = new HashSet<EXPIRED_JOURNEYS>();

    }


    public decimal STATION_ID { get; set; }

    public string STATION_NAME { get; set; }

    public string POST_CODE { get; set; }

    public string ADDRESS { get; set; }

    public decimal PLATFORM_COUNT { get; set; }

    public string STATION_STATUS { get; set; }



    public ICollection<JOURNEY> JOURNEYS { get; set; }

    public ICollection<JOURNEY> JOURNEYS1 { get; set; }

    public ICollection<EXPIRED_JOURNEYS> EXPIRED_JOURNEYS { get; set; }

    public ICollection<EXPIRED_JOURNEYS> EXPIRED_JOURNEYS1 { get; set; }

}

}