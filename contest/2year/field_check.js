function alertSetFocus (object, message) {
        alert (message);
        object.focus();
        object.select();
        return false;
}

function check_empty(textfield, req, msg) {
        if (!req) {
                return true;
        } else {
            alertSetFocus(textfield, msg);
            return false;
        }
}  

function white_space(cc)
{
	return (cc == ' ' || cc == '\n' || cc == '\t');
}

function illegal_chars(cc)
{
	legal = ((cc >= 'a' && cc <= 'z') ||
		 (cc >= 'A' && cc <= 'Z') ||
		 (cc >= '0' && cc <= '9') ||
		 cc == '!' || cc == '$' || cc == "-" ||
		 cc == '@' || cc == '*' || cc == ":" ||
		 cc == ";" || cc == ',' || cc == "." ||
		 cc == "?" || cc == "/" || cc == "_" ||
		 cc == " " || cc == "\n" || cc == "\r" ||
		 cc == "'");
	if (legal) {
		return false;
	} else {
		return true;
	}
}

function illegal_name_chars(cc)
{
	legal = ((cc >= 'a' && cc <= 'z') ||
		 (cc >= 'A' && cc <= 'Z') ||
		 (cc >= '0' && cc <= '9') ||
		 cc == "." ||
		 cc == " ");
	if (legal) {
		return false;
	} else {
		return true;
	}
}

function check_chars(textfield, msgname)
{
	string = textfield.value;
	for (var i = 0; i < string.length; i++) {
		if (illegal_chars(string.charAt(i))) {
			msg = "I'm sorry, " +
				msgname +
				" cannot contain the \'" +
				string.charAt(i) +
				"\' character.";
			alertSetFocus(textfield, msg);
			return false;
		}
	}
	return (true);
}
		
function check_name_chars(textfield, msgname)
{
	string = textfield.value;
	for (var i = 0; i < string.length; i++) {
		if (illegal_name_chars(string.charAt(i))) {
			msg = "I'm sorry, " +
				msgname +
				" cannot contain the \'" +
				string.charAt(i) +
				"\' character.";
			alertSetFocus(textfield, msg);
			return false;
		}
	}
	return (true);
}

function dump_ws(string)
{
	var ns, i, saw_char;

	ns = ""
	saw_char = false;
	i = 0;

	while (i < string.length) {
		while (white_space(string.charAt(i)) && i < string.length) {
			i++;
		}
		if (i < string.length) {
			if (saw_char) {
				ns += " ";
			}
			saw_char = true;
			while (!white_space(string.charAt(i)) &&
							i < string.length) {
				ns = ns + string.charAt(i);
				i++;
			}
		}
	}
	return (ns);
}
		
function valid_text_nowsp(textfield, name, req)
{
	if (req && textfield.value == "") {
		return check_empty(textfield, req, "I\'m sorry, " +
			name + " must be set.");
	}
	return check_chars(textfield, name);
}

function valid_text(textfield, name, req)
{
	textfield.value = dump_ws(textfield.value);
	if (req && textfield.value == "") {
		return check_empty(textfield, req, "I\'m sorry, " +
		 name + " must be set.");
	}
	return check_chars(textfield, name);
}

function valid_name_text(textfield, name, req)
{
	textfield.value = dump_ws(textfield.value);
	if (req && textfield.value == "") {
		return check_empty(textfield, req, "I\'m sorry, " +
		 name + " must be set.");
	}
	return check_name_chars(textfield, name);
}

function validateForm(form) {
	if (!valid_name_text(form.userName, "user name", true))
		return false;
	if (!valid_text(form.email, "email", false))
		return false;
	return true;
}
