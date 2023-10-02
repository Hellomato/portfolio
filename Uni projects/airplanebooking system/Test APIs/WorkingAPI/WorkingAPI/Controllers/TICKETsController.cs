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
    public class TICKETsController : ApiController
    {
        private Entities db = new Entities();

        // GET: api/TICKETs
        public IQueryable<TICKET> GetTICKETS()
        {
            return db.TICKETS;
        }

        [Route("api/TICKETS/TICKETByID")]
        public async Task<IHttpActionResult> getTICKETSByID(string id)
        {
            String queryString = "SELECT * FROM TICKETS WHERE CUSTOMER_ID = :id";
            OracleParameter parameter;
            parameter = new OracleParameter("id", id);

            List<TICKET> tickets = await db.TICKETS.SqlQuery(queryString, parameter).ToListAsync();
            if (tickets == null)
            {
                return NotFound();
            }
            return Ok(tickets);

        }

        // GET: api/TICKETs/5
        [ResponseType(typeof(TICKET))]
        public async Task<IHttpActionResult> GetTICKET(string id)
        {
            TICKET tICKET = await db.TICKETS.FindAsync(id);
            if (tICKET == null)
            {
                return NotFound();
            }

            return Ok(tICKET);
        }

        // PUT: api/TICKETs/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutTICKET(string id, TICKET tICKET)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != tICKET.TICKET_NO)
            {
                return BadRequest();
            }

            db.Entry(tICKET).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!TICKETExists(id))
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

        // POST: api/TICKETs
        [ResponseType(typeof(TICKET))]
        public async Task<IHttpActionResult> PostTICKET(TICKET tICKET)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.TICKETS.Add(tICKET);

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateException)
            {
                if (TICKETExists(tICKET.TICKET_NO))
                {
                    return Conflict();
                }
                else
                {
                    throw;
                }
            }

            return CreatedAtRoute("DefaultApi", new { id = tICKET.TICKET_NO }, tICKET);
        }

        // DELETE: api/TICKETs/5
        [ResponseType(typeof(TICKET))]
        public async Task<IHttpActionResult> DeleteTICKET(string id)
        {
            TICKET tICKET = await db.TICKETS.FindAsync(id);
            if (tICKET == null)
            {
                return NotFound();
            }

            db.TICKETS.Remove(tICKET);
            await db.SaveChangesAsync();

            return Ok(tICKET);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool TICKETExists(string id)
        {
            return db.TICKETS.Count(e => e.TICKET_NO == id) > 0;
        }
    }
}