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
using TestAPI.Models;

namespace TestAPI.Controllers
{
    [RoutePrefix("api/login")]
    public class LoginController : ApiController
    {
        private Entities db = new Entities();

        [HttpGet]
        [Route("{username}/{password}")]
        // GET: api/login/{username}/{password}
        [ResponseType(typeof(String[]))]
        public async Task<IHttpActionResult> LoginSTAFF_USERS(string username, string password)
        {
            ObjectParameter outputParameter;
            string[] returnArray;
            string returnValue;

            outputParameter = new ObjectParameter("r_value", "");

            db.VALIDATE_USER(username, password, outputParameter);

            returnValue = (string)outputParameter.Value;

            returnArray = new string[1];
            returnArray[0] = returnValue;

            if (returnArray.Length != 0)
            {
                return Ok(returnArray);
            }
            else
            {
                return StatusCode(HttpStatusCode.BadGateway);
            }
        }

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