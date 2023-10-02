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
    public class TRAINsController : ApiController
    {
        private Entities db = new Entities();

        // GET: api/TRAINs
        public IQueryable<TRAIN> GetTRAINS()
        {
            return db.TRAINS;
        }

        // GET: api/TRAINs/5
        [ResponseType(typeof(TRAIN))]
        public async Task<IHttpActionResult> GetTRAIN(decimal id)
        {
            TRAIN tRAIN = await db.TRAINS.FindAsync(id);
            if (tRAIN == null)
            {
                return NotFound();
            }

            return Ok(tRAIN);
        }

        // PUT: api/TRAINs/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutTRAIN(decimal id, TRAIN tRAIN)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != tRAIN.TRAIN_ID)
            {
                return BadRequest();
            }

            db.Entry(tRAIN).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!TRAINExists(id))
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

        // POST: api/TRAINs
        [ResponseType(typeof(TRAIN))]
        public async Task<IHttpActionResult> PostTRAIN(TRAIN tRAIN)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.TRAINS.Add(tRAIN);

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateException)
            {
                if (TRAINExists(tRAIN.TRAIN_ID))
                {
                    return Conflict();
                }
                else
                {
                    throw;
                }
            }

            return CreatedAtRoute("DefaultApi", new { id = tRAIN.TRAIN_ID }, tRAIN);
        }

        // DELETE: api/TRAINs/5
        [ResponseType(typeof(TRAIN))]
        public async Task<IHttpActionResult> DeleteTRAIN(decimal id)
        {
            TRAIN tRAIN = await db.TRAINS.FindAsync(id);
            if (tRAIN == null)
            {
                return NotFound();
            }

            db.TRAINS.Remove(tRAIN);
            await db.SaveChangesAsync();

            return Ok(tRAIN);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool TRAINExists(decimal id)
        {
            return db.TRAINS.Count(e => e.TRAIN_ID == id) > 0;
        }
    }
}