

uniform sampler2D tex;
uniform float alphaMult;

vec2 texCoord = gl_TexCoord[0].xy;

float offset = 0.5 / 128.0;

void main() {
	vec4 col = texture2D(tex, texCoord);
	vec2 st = texCoord;

	vec4 col1 = vec4(0, 1, 0.35, col.a * 3);
	vec4 col2 = vec4(0, 0.35, 1, col.a * 3);

	if (col.a > 0.7)
		gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);
	else {
		col = mix(col1, col2, st.x + st.y * 0.5);

		float a = texture2D(tex, vec2(texCoord.x + offset, texCoord.y)).a +
			texture2D(tex, vec2(texCoord.x, texCoord.y - offset)).a +
			texture2D(tex, vec2(texCoord.x - offset, texCoord.y)).a +
			texture2D(tex, vec2(texCoord.x, texCoord.y + offset)).a;
		if (col.a < 1.0 && a > 0.0)
		
			gl_FragColor = col;
		else
			gl_FragColor = col;
	}

}