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
    public class JOURNEYsController : ApiController
    {
        private Entities db = new Entities();

        // GET: api/JOURNEYs
        public IQueryable<JOURNEY> GetJOURNEYS()
        {
            return db.JOURNEYS;
        }

        // GET: api/JOURNEYs/5
        [ResponseType(typeof(JOURNEY))]
        public async Task<IHttpActionResult> GetJOURNEY(decimal id)
        {
            JOURNEY jOURNEY = await db.JOURNEYS.FindAsync(id);
            if (jOURNEY == null)
            {
                return NotFound();
            }

            return Ok(jOURNEY);
        }

        // PUT: api/JOURNEYs/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutJOURNEY(decimal id, JOURNEY jOURNEY)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != jOURNEY.JOURNEY_ID)
            {
                return BadRequest();
            }

            db.Entry(jOURNEY).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!JOURNEYExists(id))
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

        // POST: api/JOURNEYs
        [ResponseType(typeof(JOURNEY))]
        public async Task<IHttpActionResult> PostJOURNEY(JOURNEY jOURNEY)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.JOURNEYS.Add(jOURNEY);

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateException)
            {
                if (JOURNEYExists(jOURNEY.JOURNEY_ID))
                {
                    return Conflict();
                }
                else
                {
                    throw;
                }
            }

            return CreatedAtRoute("DefaultApi", new { id = jOURNEY.JOURNEY_ID }, jOURNEY);
        }

        // DELETE: api/JOURNEYs/5
        [ResponseType(typeof(JOURNEY))]
        public async Task<IHttpActionResult> DeleteJOURNEY(decimal id)
        {
            JOURNEY jOURNEY = await db.JOURNEYS.FindAsync(id);
            if (jOURNEY == null)
            {
                return NotFound();
            }

            db.JOURNEYS.Remove(jOURNEY);
            await db.SaveChangesAsync();

            return Ok(jOURNEY);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool JOURNEYExists(decimal id)
        {
            return db.JOURNEYS.Count(e => e.JOURNEY_ID == id) > 0;
        }
    }
}