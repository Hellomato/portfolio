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
    public class TIMETABLE_RECORDSController : ApiController
    {
        private Entities db = new Entities();

        // GET: api/TIMETABLE_RECORDS
        public IQueryable<TIMETABLE_RECORDS> GetTIMETABLE_RECORDS()
        {
            return db.TIMETABLE_RECORDS;
        }

        // GET: api/TIMETABLE_RECORDS/5
        [ResponseType(typeof(TIMETABLE_RECORDS))]
        public async Task<IHttpActionResult> GetTIMETABLE_RECORDS(decimal id)
        {
            TIMETABLE_RECORDS tIMETABLE_RECORDS = await db.TIMETABLE_RECORDS.FindAsync(id);
            if (tIMETABLE_RECORDS == null)
            {
                return NotFound();
            }

            return Ok(tIMETABLE_RECORDS);
        }

        // PUT: api/TIMETABLE_RECORDS/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutTIMETABLE_RECORDS(decimal id, TIMETABLE_RECORDS tIMETABLE_RECORDS)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != tIMETABLE_RECORDS.Journey_No)
            {
                return BadRequest();
            }

            db.Entry(tIMETABLE_RECORDS).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!TIMETABLE_RECORDSExists(id))
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

        // POST: api/TIMETABLE_RECORDS
        [ResponseType(typeof(TIMETABLE_RECORDS))]
        public async Task<IHttpActionResult> PostTIMETABLE_RECORDS(TIMETABLE_RECORDS tIMETABLE_RECORDS)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.TIMETABLE_RECORDS.Add(tIMETABLE_RECORDS);

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateException)
            {
                if (TIMETABLE_RECORDSExists(tIMETABLE_RECORDS.Journey_No))
                {
                    return Conflict();
                }
                else
                {
                    throw;
                }
            }

            return CreatedAtRoute("DefaultApi", new { id = tIMETABLE_RECORDS.Journey_No }, tIMETABLE_RECORDS);
        }

        // DELETE: api/TIMETABLE_RECORDS/5
        [ResponseType(typeof(TIMETABLE_RECORDS))]
        public async Task<IHttpActionResult> DeleteTIMETABLE_RECORDS(decimal id)
        {
            TIMETABLE_RECORDS tIMETABLE_RECORDS = await db.TIMETABLE_RECORDS.FindAsync(id);
            if (tIMETABLE_RECORDS == null)
            {
                return NotFound();
            }

            db.TIMETABLE_RECORDS.Remove(tIMETABLE_RECORDS);
            await db.SaveChangesAsync();

            return Ok(tIMETABLE_RECORDS);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool TIMETABLE_RECORDSExists(decimal id)
        {
            return db.TIMETABLE_RECORDS.Count(e => e.Journey_No == id) > 0;
        }
    }
}