using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Core.Objects;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Description;
using WorkingAPI.Models;

namespace WorkingAPI.Controllers
{
    [RoutePrefix("api/login")]
    public class LoginController : ApiController
    {
        private Entities db = new Entities();

        [HttpPost]
        [ResponseType(typeof(String[]))]
        //[ValidateAntiForgeryToken]
        public async Task<IHttpActionResult> LoginProcedure([FromBody]LoginModel objUser)
        {
            ObjectParameter outputParameter;
            string returnValue;

            outputParameter = new ObjectParameter("r_value", "");

            db.VALIDATE_USER(objUser.USERNAME, objUser.PASSWORD, outputParameter);

            returnValue = (string)outputParameter.Value;

            if (returnValue == "1")
            {
                return Ok();
            }
            else
            {
                return StatusCode(HttpStatusCode.Forbidden);
            }
        }

    }
}