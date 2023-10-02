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
    public class EXPIRED_TICKETSController : ApiController
    {
        private Entities db = new Entities();

        // GET: api/EXPIRED_TICKETS
        public IQueryable<EXPIRED_TICKETS> GetEXPIRED_TICKETS()
        {
            return db.EXPIRED_TICKETS;
        }

        [Route("api/EXPIRED_TICKETS/EXPIRED_TICKETByID")]
        public async Task<IHttpActionResult> getEXPIRED_TICKETSByID(string id)
        {
            String queryString = "SELECT * FROM EXPIRED_TICKETS WHERE CUSTOMER_ID = :id";
            OracleParameter parameter;
            parameter = new OracleParameter("id", id);

            List<EXPIRED_TICKETS> expired_tickets = await db.EXPIRED_TICKETS.SqlQuery(queryString, parameter).ToListAsync();
            if (expired_tickets == null)
            {
                return NotFound();
            }
            return Ok(expired_tickets);

        }


        // GET: api/EXPIRED_TICKETS/5
        [ResponseType(typeof(EXPIRED_TICKETS))]
        public async Task<IHttpActionResult> GetEXPIRED_TICKETS(string id)
        {
            EXPIRED_TICKETS eXPIRED_TICKETS = await db.EXPIRED_TICKETS.FindAsync(id);
            if (eXPIRED_TICKETS == null)
            {
                return NotFound();
            }

            return Ok(eXPIRED_TICKETS);
        }

        // PUT: api/EXPIRED_TICKETS/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutEXPIRED_TICKETS(string id, EXPIRED_TICKETS eXPIRED_TICKETS)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != eXPIRED_TICKETS.TICKET_NO)
            {
                return BadRequest();
            }

            db.Entry(eXPIRED_TICKETS).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!EXPIRED_TICKETSExists(id))
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

        // POST: api/EXPIRED_TICKETS
        [ResponseType(typeof(EXPIRED_TICKETS))]
        public async Task<IHttpActionResult> PostEXPIRED_TICKETS(EXPIRED_TICKETS eXPIRED_TICKETS)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.EXPIRED_TICKETS.Add(eXPIRED_TICKETS);

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateException)
            {
                if (EXPIRED_TICKETSExists(eXPIRED_TICKETS.TICKET_NO))
                {
                    return Conflict();
                }
                else
                {
                    throw;
                }
            }

            return CreatedAtRoute("DefaultApi", new { id = eXPIRED_TICKETS.TICKET_NO }, eXPIRED_TICKETS);
        }

        // DELETE: api/EXPIRED_TICKETS/5
        [ResponseType(typeof(EXPIRED_TICKETS))]
        public async Task<IHttpActionResult> DeleteEXPIRED_TICKETS(string id)
        {
            EXPIRED_TICKETS eXPIRED_TICKETS = await db.EXPIRED_TICKETS.FindAsync(id);
            if (eXPIRED_TICKETS == null)
            {
                return NotFound();
            }

            db.EXPIRED_TICKETS.Remove(eXPIRED_TICKETS);
            await db.SaveChangesAsync();

            return Ok(eXPIRED_TICKETS);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool EXPIRED_TICKETSExists(string id)
        {
            return db.EXPIRED_TICKETS.Count(e => e.TICKET_NO == id) > 0;
        }
    }
}