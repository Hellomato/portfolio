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
    public class STATIONsController : ApiController
    {
        private Entities db = new Entities();

        // GET: api/STATIONs
        public IQueryable<STATION> GetSTATIONS()
        {
            return db.STATIONS;
        }

        // GET: api/STATIONs/5
        [ResponseType(typeof(STATION))]
        public async Task<IHttpActionResult> GetSTATION(decimal id)
        {
            STATION sTATION = await db.STATIONS.FindAsync(id);
            if (sTATION == null)
            {
                return NotFound();
            }

            return Ok(sTATION);
        }

        // PUT: api/STATIONs/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutSTATION(decimal id, STATION sTATION)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != sTATION.STATION_ID)
            {
                return BadRequest();
            }

            db.Entry(sTATION).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!STATIONExists(id))
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

        // POST: api/STATIONs
        [ResponseType(typeof(STATION))]
        public async Task<IHttpActionResult> PostSTATION(STATION sTATION)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.STATIONS.Add(sTATION);

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateException)
            {
                if (STATIONExists(sTATION.STATION_ID))
                {
                    return Conflict();
                }
                else
                {
                    throw;
                }
            }

            return CreatedAtRoute("DefaultApi", new { id = sTATION.STATION_ID }, sTATION);
        }

        // DELETE: api/STATIONs/5
        [ResponseType(typeof(STATION))]
        public async Task<IHttpActionResult> DeleteSTATION(decimal id)
        {
            STATION sTATION = await db.STATIONS.FindAsync(id);
            if (sTATION == null)
            {
                return NotFound();
            }

            db.STATIONS.Remove(sTATION);
            await db.SaveChangesAsync();

            return Ok(sTATION);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool STATIONExists(decimal id)
        {
            return db.STATIONS.Count(e => e.STATION_ID == id) > 0;
        }
    }
}