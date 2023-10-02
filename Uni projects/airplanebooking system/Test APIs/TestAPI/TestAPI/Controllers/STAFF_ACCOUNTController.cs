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
using TestAPI.Models;

namespace TestAPI.Controllers
{
    public class STAFF_ACCOUNTController : ApiController
    {
        private Entities db = new Entities();

        // GET: api/STAFF_ACCOUNT
        public IQueryable<STAFF_ACCOUNT> GetSTAFF_ACCOUNT()
        {
            return db.STAFF_ACCOUNT;
        }

        // GET: api/STAFF_ACCOUNT/5
        [ResponseType(typeof(STAFF_ACCOUNT))]
        public async Task<IHttpActionResult> GetSTAFF_ACCOUNT(decimal id)
        {
            STAFF_ACCOUNT sTAFF_ACCOUNT = await db.STAFF_ACCOUNT.FindAsync(id);
            if (sTAFF_ACCOUNT == null)
            {
                return NotFound();
            }

            return Ok(sTAFF_ACCOUNT);
        }

        // PUT: api/STAFF_ACCOUNT/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutSTAFF_ACCOUNT(decimal id, STAFF_ACCOUNT sTAFF_ACCOUNT)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != sTAFF_ACCOUNT.STAFF_ID)
            {
                return BadRequest();
            }

            db.Entry(sTAFF_ACCOUNT).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!STAFF_ACCOUNTExists(id))
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

        // POST: api/STAFF_ACCOUNT
        [ResponseType(typeof(STAFF_ACCOUNT))]
        public async Task<IHttpActionResult> PostSTAFF_ACCOUNT(STAFF_ACCOUNT sTAFF_ACCOUNT)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.STAFF_ACCOUNT.Add(sTAFF_ACCOUNT);

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateException)
            {
                if (STAFF_ACCOUNTExists(sTAFF_ACCOUNT.STAFF_ID))
                {
                    return Conflict();
                }
                else
                {
                    throw;
                }
            }

            return CreatedAtRoute("DefaultApi", new { id = sTAFF_ACCOUNT.STAFF_ID }, sTAFF_ACCOUNT);
        }

        // DELETE: api/STAFF_ACCOUNT/5
        [ResponseType(typeof(STAFF_ACCOUNT))]
        public async Task<IHttpActionResult> DeleteSTAFF_ACCOUNT(decimal id)
        {
            STAFF_ACCOUNT sTAFF_ACCOUNT = await db.STAFF_ACCOUNT.FindAsync(id);
            if (sTAFF_ACCOUNT == null)
            {
                return NotFound();
            }

            db.STAFF_ACCOUNT.Remove(sTAFF_ACCOUNT);
            await db.SaveChangesAsync();

            return Ok(sTAFF_ACCOUNT);
        }

        // GET: api/STAFF_ACCOUNT/5
        [ResponseType(typeof(STAFF_ACCOUNT))]
        public async Task<IHttpActionResult> GetSTAFF_ACCOUNT(string username)
        {
            STAFF_ACCOUNT sTAFF_ACCOUNT = await db.STAFF_ACCOUNT.SqlQuery($"SELECT * FROM PRCS251J.STAFF_ACCOUNT WHERE USERNAME = '{username}'").FirstAsync();
            if (sTAFF_ACCOUNT == null)
            {
                return NotFound();
            }

            return Ok(sTAFF_ACCOUNT);
        }

        // POST: api/STAFF_ACCOUNT
        [ResponseType(typeof(STAFF_ACCOUNT))]
        public async Task<IHttpActionResult> PostSTAFF_ACCOUNT(string username)
        {

            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            STAFF_ACCOUNT sTAFF_ACCOUNT = await db.STAFF_ACCOUNT.SqlQuery($"SELECT * FROM PRCS251J.STAFF_ACCOUNT WHERE USERNAME = '{username}'").FirstAsync();
            if (sTAFF_ACCOUNT == null)
            {
                return NotFound();
            }
            return Ok();
        }

        // POST: api/LoginSTAFF_ACCOUNT
        [ResponseType(typeof(STAFF_ACCOUNT))]
        public async Task<IHttpActionResult> PostLoginSTAFF_ACCOUNT(string username, string password)
        {

            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            STAFF_ACCOUNT sTAFF_ACCOUNT = await db.STAFF_ACCOUNT.SqlQuery($"SELECT * FROM PRCS251J.STAFF_ACCOUNT WHERE USERNAME = '{username}' AND PASSWORD = '{password}'").FirstAsync();
            if (sTAFF_ACCOUNT == null)
            {
                
                return NotFound();
                
            }
            return Ok();
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool STAFF_ACCOUNTExists(decimal id)
        {
            return db.STAFF_ACCOUNT.Count(e => e.STAFF_ID == id) > 0;
        }
    }
}