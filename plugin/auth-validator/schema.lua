local typedefs = require "kong.db.schema.typedefs"

return {
  name = "auth-validator",
  fields = {
    -- This plugin is not bound to a consumer
    { consumer = typedefs.no_consumer },

    -- Plugin works only with HTTP and HTTPS protocols
    { protocols = typedefs.protocols_http },

    -- Configuration options
    { config = {
        type = "record",
        fields = {
          {
            auth_service_url = {
              type = "string",
              required = true,
              default = "http://authservice:9898/auth/v1/ping",
              description = "The full URL of the auth service endpoint to validate tokens"
            }
          },
        },
      },
    },
  },
}
