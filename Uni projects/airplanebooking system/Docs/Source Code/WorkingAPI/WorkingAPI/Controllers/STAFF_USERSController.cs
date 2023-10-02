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
    public class STAFF_USERSController : ApiController
    {
        private Entities db = new Entities();

        // GET: api/STAFF_USERS
        public IQueryable<STAFF_USERS> GetSTAFF_USERS()
        {
            return db.STAFF_USERS;
        }

        // GET: api/STAFF_USERS/5
        [ResponseType(typeof(STAFF_USERS))]
        public async Task<IHttpActionResult> GetSTAFF_USERS(decimal id)
        {
            STAFF_USERS sTAFF_USERS = await db.STAFF_USERS.FindAsync(id);
            if (sTAFF_USERS == null)
            {
                return NotFound();
            }

            return Ok(sTAFF_USERS);
        }

        // GET: api/STAFF/getID
        [Route("api/STAFF/getID")]
        public async Task<IHttpActionResult> GetCUSTOMERID(string username)
        {
            STAFF_USERS sTAFF_USERS;

            String queryString = "SELECT * FROM STAFF_USERS WHERE UPPER(USERNAME) = UPPER(:username) ";

            OracleParameter parameter;
            parameter = new OracleParameter("username", username);

            sTAFF_USERS = await db.STAFF_USERS.SqlQuery(queryString, parameter).FirstAsync();

            if (sTAFF_USERS == null)
            {
                return NotFound();
            }
            else
            {
                return Ok(sTAFF_USERS.STAFF_ID);
            }
        }

        // PUT: api/STAFF_USERS/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutSTAFF_USERS(decimal id, STAFF_USERS sTAFF_USERS)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != sTAFF_USERS.STAFF_ID)
            {
                return BadRequest();
            }

            db.Entry(sTAFF_USERS).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!STAFF_USERSExists(id))
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

        // POST: api/STAFF_USERS
        [ResponseType(typeof(STAFF_USERS))]
        public async Task<IHttpActionResult> PostSTAFF_USERS(STAFF_USERS sTAFF_USERS)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.STAFF_USERS.Add(sTAFF_USERS);

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateException)
            {
                if (STAFF_USERSExists(sTAFF_USERS.STAFF_ID))
                {
                    return Conflict();
                }
                else
                {
                    throw;
                }
            }

            return CreatedAtRoute("DefaultApi", new { id = sTAFF_USERS.STAFF_ID }, sTAFF_USERS);
        }

        // DELETE: api/STAFF_USERS/5
        [ResponseType(typeof(STAFF_USERS))]
        public async Task<IHttpActionResult> DeleteSTAFF_USERS(decimal id)
        {
            STAFF_USERS sTAFF_USERS = await db.STAFF_USERS.FindAsync(id);
            if (sTAFF_USERS == null)
            {
                return NotFound();
            }

            db.STAFF_USERS.Remove(sTAFF_USERS);
            await db.SaveChangesAsync();

            return Ok(sTAFF_USERS);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool STAFF_USERSExists(decimal id)
        {
            return db.STAFF_USERS.Count(e => e.STAFF_ID == id) > 0;
        }
    }
}