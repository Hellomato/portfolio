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
    public class HISTORY_RECORDSController : ApiController
    {
        private Entities db = new Entities();

        // GET: api/HISTORY_RECORDS
        public IQueryable<HISTORY_RECORDS> GetHISTORY_RECORDS()
        {
            return db.HISTORY_RECORDS;
        }

        [Route("api/HISTORY_RECORDS/search")]
        // GET: api/HISTORY_RECORDS?journeyNo={Journey_No}
        [ResponseType(typeof(HISTORY_RECORDS))]
        public async Task<IHttpActionResult> GetHISTORY_RECORDS(decimal? journeyNo)
        {
            string queryString;

            OracleParameter parameter;

            queryString = "SELECT * FROM PRCS251J.HISTORY_RECORDS WHERE JOURNEY_NO LIKE '%' || :journeyNo || '%' ";

            parameter = new OracleParameter("journeyNo", journeyNo);

            List<HISTORY_RECORDS> hISTORY_RECORDS = await db.HISTORY_RECORDS.SqlQuery(queryString, parameter).ToListAsync();

            if (hISTORY_RECORDS == null)
            {
                return NotFound();
            }

            return Ok(hISTORY_RECORDS);
        }

        // GET: api/HISTORY_RECORDS/5
        [ResponseType(typeof(HISTORY_RECORDS))]
        public async Task<IHttpActionResult> GetHISTORY_RECORDS(decimal id)
        {
            HISTORY_RECORDS hISTORY_RECORDS = await db.HISTORY_RECORDS.FindAsync(id);
            if (hISTORY_RECORDS == null)
            {
                return NotFound();
            }

            return Ok(hISTORY_RECORDS);
        }

        // PUT: api/HISTORY_RECORDS/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutHISTORY_RECORDS(decimal id, HISTORY_RECORDS hISTORY_RECORDS)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != hISTORY_RECORDS.JOURNEY_NO)
            {
                return BadRequest();
            }

            db.Entry(hISTORY_RECORDS).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!HISTORY_RECORDSExists(id))
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

        // POST: api/HISTORY_RECORDS
        [ResponseType(typeof(HISTORY_RECORDS))]
        public async Task<IHttpActionResult> PostHISTORY_RECORDS(HISTORY_RECORDS hISTORY_RECORDS)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.HISTORY_RECORDS.Add(hISTORY_RECORDS);

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateException)
            {
                if (HISTORY_RECORDSExists(hISTORY_RECORDS.JOURNEY_NO))
                {
                    return Conflict();
                }
                else
                {
                    throw;
                }
            }

            return CreatedAtRoute("DefaultApi", new { id = hISTORY_RECORDS.JOURNEY_NO }, hISTORY_RECORDS);
        }

        // DELETE: api/HISTORY_RECORDS/5
        [ResponseType(typeof(HISTORY_RECORDS))]
        public async Task<IHttpActionResult> DeleteHISTORY_RECORDS(decimal id)
        {
            HISTORY_RECORDS hISTORY_RECORDS = await db.HISTORY_RECORDS.FindAsync(id);
            if (hISTORY_RECORDS == null)
            {
                return NotFound();
            }

            db.HISTORY_RECORDS.Remove(hISTORY_RECORDS);
            await db.SaveChangesAsync();

            return Ok(hISTORY_RECORDS);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool HISTORY_RECORDSExists(decimal id)
        {
            return db.HISTORY_RECORDS.Count(e => e.JOURNEY_NO == id) > 0;
        }
    }
}