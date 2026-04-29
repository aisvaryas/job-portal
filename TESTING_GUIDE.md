# Quick Testing Guide - Profile CRUD Operations

## Prerequisites
- Backend running on `http://localhost:8084`
- Frontend running on dev server
- MySQL database connected
- MinIO running for resume storage
- Valid user token in localStorage

## Quick Test Cases

### 1. Add Skill (Most Common Issue)
**Test:** Add skill "JavaScript"
```bash
curl -X POST http://localhost:8084/seekerskill/save/user@example.com \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{"skillName":"JavaScript"}'
```

**Expected Response:**
```json
{
  "message": "Skill Added Successfully",
  "status": 201,
  "data": {
    "id": 1,
    "skillName": "JavaScript"
  }
}
```

### 2. Add Education
**Test:** Add degree
```bash
curl -X POST http://localhost:8084/education/save/user@example.com \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "institution": "MIT",
    "degree": "B.S.",
    "fieldOfStudy": "Computer Science",
    "graduationDate": "2023-05-15",
    "grade": "A",
    "description": "Focused on AI"
  }'
```

**Expected:** 201 Created

### 3. Add Experience
**Test:** Add work experience
```bash
curl -X POST http://localhost:8084/experience/save/user@example.com \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "company": "Tech Corp",
    "position": "Software Engineer",
    "startDate": "2022-01-15",
    "endDate": "2024-03-20",
    "location": "San Francisco",
    "description": "Worked on APIs"
  }'
```

**Expected:** 201 Created

### 4. Upload Resume
**Test:** Upload PDF resume
```bash
curl -X POST http://localhost:8084/resume/upload/user@example.com \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -F "file=@resume.pdf" \
  -F "isDefault=true" \
  -F "visibility=PRIVATE"
```

**Expected:** 201 Created with file URL

### 5. Get All Skills
**Test:** Retrieve skills
```bash
curl -X GET http://localhost:8084/seekerskill/all/user@example.com \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Expected Response:**
```json
{
  "message": "Skills Fetched Successfully",
  "status": 200,
  "data": [
    {"id": 1, "skillName": "JavaScript"},
    {"id": 2, "skillName": "React"}
  ]
}
```

## Error Test Cases

### Test Invalid Input (Should Fail)
```bash
# Missing skill name
curl -X POST http://localhost:8084/seekerskill/save/user@example.com \
  -H "Content-Type: application/json" \
  -d '{}'

# Expected: 400 Bad Request - "Skill name cannot be empty"
```

### Test Invalid Email (Should Fail)
```bash
curl -X POST http://localhost:8084/education/save/nonexistent@example.com \
  -H "Content-Type: application/json" \
  -d '{"institution":"MIT","degree":"B.S.","graduationDate":"2023-05-15"}'

# Expected: 404 Not Found - "Job seeker not found with email"
```

### Test Invalid Dates (Should Fail)
```bash
curl -X POST http://localhost:8084/experience/save/user@example.com \
  -H "Content-Type: application/json" \
  -d '{
    "company":"Tech",
    "position":"Dev",
    "startDate":"2024-01-01",
    "endDate":"2023-01-01"
  }'

# Expected: 400 Bad Request - "End date cannot be before start date"
```

## Frontend Testing

1. **Open Profile Page**
   - Should load existing skills, education, experience
   - Should handle empty lists gracefully

2. **Add New Skill**
   - Type "Python" in skill input
   - Click "Add Skill"
   - Should appear immediately in skills list
   - Should show success message

3. **Add Education**
   - Fill all required fields (Institution, Degree, Graduation Date)
   - Click "Add Education"
   - Should appear in education section
   - Dates should format as YYYY-MM-DD

4. **Add Experience**
   - Fill Company, Position, Start Date
   - End Date is optional
   - Should validate that end date > start date
   - Should appear in experience section

5. **Upload Resume**
   - Select PDF, DOC, or DOCX file
   - Click "Upload Resume"
   - Should show success confirmation
   - Should be retrievable

## Debugging Tips

### Check Browser Console
- Look for API errors
- Check response status codes
- Verify token is being sent

### Check Browser Network Tab
- Verify request headers include Authorization
- Check response headers for CORS issues
- Verify Content-Type headers match payload

### Check Server Logs
```bash
# For development, tail logs:
tail -f app.log | grep ERROR
tail -f app.log | grep "Job seeker not found"
```

## Success Indicators

✅ All CRUD operations return proper status codes (201, 200, 404, 400)
✅ Error messages are descriptive and actionable
✅ Data persists across page reloads
✅ Frontend displays data correctly
✅ Dates format correctly
✅ No null pointer exceptions in logs
✅ Ownership checks prevent unauthorized access

## Common Issues & Solutions

| Issue | Cause | Solution |
|-------|-------|----------|
| 404 "Job seeker not found" | Email mismatch | Verify user email matches in request |
| Null skillName in response | Old mapper | Redeploy backend, clear cache |
| Date parse error | Wrong format | Ensure YYYY-MM-DD format |
| Duplicate skill error | Expected behavior | Remove old skill first |
| Empty data on reload | API error | Check backend logs for exceptions |
