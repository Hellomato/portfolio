function value = reward( state, action )
%Give reward if input correct

if state == 5 && action == 3
    value = 10.0;
else
    value = 0.0;
end

end

