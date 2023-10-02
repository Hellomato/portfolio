using Oracle.ManagedDataAccess.Client;
using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
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
    public class CUSTOMERsController : ApiController
    {
        private Entities db = new Entities();

        // GET: api/CUSTOMERs
        public IQueryable<CUSTOMER> GetCUSTOMERS()
        {
            return db.CUSTOMERS;
        }

        // GET: api/CUSTOMER/getID
        [Route("api/CUSTOMER/getID")]
        public async Task<IHttpActionResult> GetCUSTOMERID(string username)
        {
            CUSTOMER cUSTOMER;

            String queryString = "SELECT * FROM CUSTOMERS WHERE UPPER(USERNAME) = UPPER(:username) ";

            OracleParameter parameter;
            parameter = new OracleParameter("username", username);

            cUSTOMER = await db.CUSTOMERS.SqlQuery(queryString, parameter).FirstAsync();

            if (cUSTOMER == null)
            {
                return NotFound();
            }
            else
            {
                return Ok(cUSTOMER.CUSTOMER_ID);
            }
        }

        // GET: api/CUSTOMERs/5
        [ResponseType(typeof(CUSTOMER))]
        public async Task<IHttpActionResult> GetCUSTOMER(decimal id)
        {
            CUSTOMER cUSTOMER = await db.CUSTOMERS.FindAsync(id);
            if (cUSTOMER == null)
            {
                return NotFound();
            }

            return Ok(cUSTOMER);
        }

        // PUT: api/CUSTOMERs/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutCUSTOMER(decimal id, CUSTOMER cUSTOMER)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != cUSTOMER.CUSTOMER_ID)
            {
                return BadRequest();
            }

            db.Entry(cUSTOMER).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!CUSTOMERExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return StatusCode(HttpStatusCode.NoContent);
        }

        // POST: api/CUSTOMERs
        [ResponseType(typeof(CUSTOMER))]
        public async Task<IHttpActionResult> PostCUSTOMER(CUSTOMER cUSTOMER)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.CUSTOMERS.Add(cUSTOMER);

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateException)
            {
                if (CUSTOMERExists(cUSTOMER.CUSTOMER_ID))
                {
                    return Conflict();
                }
                else
                {
                    throw;
                }
            }

            return CreatedAtRoute("DefaultApi", new { id = cUSTOMER.CUSTOMER_ID }, cUSTOMER);
        }

        // DELETE: api/CUSTOMERs/5
        [ResponseType(typeof(CUSTOMER))]
        public async Task<IHttpActionResult> DeleteCUSTOMER(decimal id)
        {
            CUSTOMER cUSTOMER = await db.CUSTOMERS.FindAsync(id);
            if (cUSTOMER == null)
            {
                return NotFound();
            }

            db.CUSTOMERS.Remove(cUSTOMER);
            await db.SaveChangesAsync();

            return Ok(cUSTOMER);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool CUSTOMERExists(decimal id)
        {
            return db.CUSTOMERS.Count(e => e.CUSTOMER_ID == id) > 0;
        }
    }
}