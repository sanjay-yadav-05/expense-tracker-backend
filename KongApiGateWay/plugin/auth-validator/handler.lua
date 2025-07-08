local kong = kong
local http = require "resty.http"
local cjson = require "cjson.safe"

local CustomAuthHandler = {
  PRIORITY = 1000,
  VERSION = "1.0",
}

function CustomAuthHandler:access(config)
  local request_path = kong.request.get_path()

  -- Allow unauthenticated routes
  local unauthenticated_paths = {
    "/auth/v1/login",
    "/auth/v1/signup",
    "/auth/v1/refreshToken"
  }

  for _, path in ipairs(unauthenticated_paths) do
    if string.find(request_path, path, 1, true) then
      return
    end
  end

  -- Get tokens from headers
  local access_token = kong.request.get_header("Authorization")

  if not access_token then
    return kong.response.exit(401, { message = "Missing tokens" })
  end

  -- Ping AuthService
  local httpc = http.new()
  httpc:set_timeouts(10000, 10000, 10000)

  local res, err = httpc:request_uri(config.auth_service_url, {
    method = "POST",
    headers = {
      ["Authorization"] = access_token,
      ["Content-Type"] = "application/json"
    },
    ssl_verify = false
  })

  if not res then
    kong.log.err("Auth service request failed: ", err)
    return kong.response.exit(500, { message = "Auth service error from kong" })
  end

  if res.status ~= 200 then
    return kong.response.exit(401, { message = "Unauthorized kong" })
  end

  -- Parse JSON response
  local body, decode_err = cjson.decode(res.body)
  if not body or not body.user_id then
    kong.log.err("Invalid response from auth service kong: ", decode_err)
    return kong.response.exit(500, { message = "Invalid response from auth service kong" })
  end

  -- Set user_id header
  kong.service.request.set_header("x-user-id", body.user_id)
end

return CustomAuthHandler
