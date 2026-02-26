exports.handler = async function (context, event, callback) {

  const client = context.getTwilioClient();
  const syncServiceSid = context.SYNC_SERVICE_SID;
  const syncMapName = context.SYNC_MAP_SCHEDULE_SID;

  const requestId = `batch_${Date.now()}`;

  try {

    const body = typeof event === "string"
      ? JSON.parse(event)
      : event;

    const { timeZone, businessHoursConfigId, items } = body;

    if (!items || !Array.isArray(items) || items.length === 0) {
      return callback(null, {
        requestId,
        error: "Items array is required"
      });
    }

    // Obtener schedule activo (o por ID)
    const schedules = await client.sync
      .services(syncServiceSid)
      .syncMaps(syncMapName)
      .syncMapItems
      .list({ limit: 100 });

    const schedule = schedules
      .map(i => i.data)
      .find(s => s.id === businessHoursConfigId || s.active === true);

    if (!schedule) {
      throw new Error("Schedule not found");
    }

    const results = items.map(item => {

      try {

        const date = new Date(item.timestamp);

        const formatter = new Intl.DateTimeFormat("en-US", {
          timeZone: timeZone || schedule.timeZone,
          weekday: "long",
          hour: "2-digit",
          minute: "2-digit",
          hour12: false
        });

        const parts = formatter.formatToParts(date);

        const currentDay = parts.find(p => p.type === "weekday").value;
        const hour = parts.find(p => p.type === "hour").value;
        const minute = parts.find(p => p.type === "minute").value;

        const currentTime = `${hour}:${minute}`;

        const todaySchedule = schedule.weeklySchedule.find(d => d.day === currentDay);

        let inHours = false;

        if (todaySchedule && todaySchedule.hasSlots) {
          inHours = todaySchedule.slots.some(slot =>
            currentTime >= slot.startTime.substring(0,5) &&
            currentTime <= slot.endTime.substring(0,5)
          );
        }

        console.log({
          requestId,
          itemId: item.itemId,
          CallSid: item.context?.CallSid,
          inHours
        });

        return {
          itemId: item.itemId,
          inHours,
          evaluatedAt: item.timestamp
        };

      } catch (itemError) {

        return {
          itemId: item.itemId,
          error: itemError.message
        };
      }
    });

    console.log({
      requestId,
      batchSize: items.length
    });

    return callback(null, {
      requestId,
      results
    });

  } catch (error) {

    console.error({
      requestId,
      error: error.message
    });

    // Fallback controlado
    return callback(null, {
      requestId,
      error: "Batch evaluation failed",
      fallback: true
    });
  }
};