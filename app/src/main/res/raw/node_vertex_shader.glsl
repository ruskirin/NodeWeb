uniform mat4 u_ProjectionMat;

attribute vec4 a_NodeCenter;
attribute vec4 a_NodeVertexColor;
attribute vec2 a_NodeTexCoords;

varying vec2 v_NodeTexCoords;

varying vec4 v_NodeVertexColor;

void main() {
    v_NodeVertexColor = a_NodeVertexColor;

    gl_Position = u_ProjectionMat * a_NodeCenter;

    v_NodeTexCoords = a_NodeTexCoords;
}