# 🚨 SOS Email Configuration Guide

## Setup Instructions

To enable email notifications for SOS alerts, you need to configure email settings in the application.properties file.

### Option 1: Using Gmail (Recommended for Testing)

1. **Enable 2-Step Verification** on your Gmail account:
   - Go to https://myaccount.google.com/security
   - Enable 2-Step Verification

2. **Generate App Password**:
   - Visit https://myaccount.google.com/apppasswords
   - Select "Mail" and your device
   - Copy the generated 16-character password

3. **Update application.properties**:
   ```properties
   spring.mail.username=your-email@gmail.com
   spring.mail.password=your-16-char-app-password
   ```

### Option 2: Using Other Email Providers

#### Outlook/Hotmail
```properties
spring.mail.host=smtp-mail.outlook.com
spring.mail.port=587
spring.mail.username=your-email@outlook.com
spring.mail.password=your-password
```

#### Yahoo Mail
```properties
spring.mail.host=smtp.mail.yahoo.com
spring.mail.port=587
spring.mail.username=your-email@yahoo.com
spring.mail.password=your-app-password
```

### Testing SOS Feature

1. **Set Emergency Contact**:
   - Login to the application
   - Navigate to `/sos-alerts` page
   - Set your emergency contact email

2. **Trigger SOS**:
   - Click the red SOS button (bottom-right corner)
   - Allow location access when prompted
   - Email will be sent to both your email and emergency contact

3. **View SOS History**:
   - Go to `/sos-alerts` to see all your SOS alerts
   - View location on Google Maps
   - Check alert status

### Features

✅ **Real-time Location Capture** - Uses browser geolocation API  
✅ **Email Notifications** - Sends to user and emergency contact  
✅ **Google Maps Integration** - View exact location on map  
✅ **Alert History** - Track all SOS alerts  
✅ **Status Tracking** - PENDING, ACKNOWLEDGED, RESOLVED  
✅ **Animated SOS Button** - Eye-catching pulsing design

### Database Table

The SOS feature creates a `sos_alerts` table with:
- User email and name
- Latitude and longitude
- Location address (Google Maps link)
- Timestamp
- Status
- Emergency contact email

### API Endpoints

- `POST /api/sos/send` - Send SOS alert
- `GET /api/sos/alerts/{email}` - Get user's SOS alerts
- `GET /api/sos/alerts` - Get all SOS alerts (Admin)
- `PUT /api/sos/alerts/{id}/status` - Update alert status

### Troubleshooting

**Email not sending?**
- Check if Gmail App Password is correct
- Verify SMTP settings in application.properties
- Check firewall/antivirus blocking port 587
- Review backend console logs for errors

**Location not working?**
- Allow location access in browser
- Use HTTPS in production (geolocation requires secure context)
- Check browser location settings

**Backend not receiving request?**
- Ensure backend is running on port 8080
- Check CORS configuration
- Verify network/firewall settings

### Production Deployment

For production, consider:
1. Use environment variables for email credentials
2. Set up dedicated SMTP service (SendGrid, AWS SES, Mailgun)
3. Implement rate limiting for SOS requests
4. Add webhook integration with emergency services
5. Store encrypted location data
6. Implement real-time websocket notifications

---

Created for HealthCare Emergency Management System
